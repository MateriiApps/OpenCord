package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.emoji.DomainEmoji
import com.xinto.opencord.domain.emoji.DomainEmojiIdentifier
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.DomainMessageRegular
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.store.*
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import com.xinto.opencord.util.collectIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Stable
class ChatViewModel(
    private val messageStore: MessageStore,
    private val reactionStore: ReactionStore,
    private val channelStore: ChannelStore,
    private val currentUserStore: CurrentUserStore,
    private val api: DiscordApiService,
    private val persistentDataManager: PersistentDataManager,
) : BasePersistenceViewModel(persistentDataManager) {
    sealed interface State {
        object Unselected : State
        object Loading : State
        object Loaded : State
        object Error : State
    }

    @Stable
    class ReactionState(
        val emoji: DomainEmoji,
        val reactionOrder: Long,
        meReacted: Boolean,
        count: Int,
    ) {
        var meReacted by mutableStateOf(meReacted)
        var count by mutableStateOf(count)
    }

    @Stable
    inner class MessageItem(
        message: DomainMessage,
        reactions: List<ReactionState>? = null,
        topMerged: Boolean = false,
    ) {
        var topMerged by mutableStateOf(topMerged)
        var bottomMerged by mutableStateOf(false)
        var message by mutableStateOf(message)
        var reactions = mutableStateMapOf<DomainEmojiIdentifier, ReactionState>()
            .apply { reactions?.let { putAll(it.map { r -> r.emoji.identifier to r }) } }

        val meMentioned by derivedStateOf {
            when {
                message !is DomainMessageRegular -> false
                message.mentionEveryone -> true
                message.mentions.any { it.id == currentUserId } -> true
                else -> false
            }
        }
    }

    var state by mutableStateOf<State>(State.Unselected)
        private set

    var channelName by mutableStateOf("")
        private set
    var currentUserId by mutableStateOf<Long?>(null)
        private set

    // Reverse sorted (decreasing) message list
    val sortedMessages = mutableStateListOf<MessageItem>()

    private fun getMessageItemIndex(messageId: Long): Int? {
        return sortedMessages
            .binarySearch { messageId compareTo it.message.id }
            .takeIf { it >= 0 }
    }

    fun load() {
        val channelId = persistentDataManager.persistentChannelId
        if (channelId <= 0L || persistentDataManager.persistentGuildId <= 0L) return

        viewModelScope.coroutineContext.cancelChildren()

        state = State.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val channel = channelStore.fetchChannel(channelId)
                    ?: throw Error("Failed to load channel $channelId")
                val messages = messageStore.fetchMessages(channelId)
                    .sortedByDescending { it.id }

                val messageItems = messages.map {
                    val reactions = reactionStore.getReactions(it.id).mapIndexed { i, reaction ->
                        ReactionState(
                            reactionOrder = i.toLong(),
                            emoji = reaction.emoji,
                            count = reaction.count,
                            meReacted = reaction.meReacted,
                        )
                    }

                    MessageItem(
                        message = it,
                        reactions = reactions,
                    )
                }

                for (i in 0 until (messageItems.size - 1)) {
                    val curMessage = messageItems[i]
                    val prevMessage = messageItems[i + 1]

                    val canMerge = canMessagesMerge(curMessage.message, prevMessage.message)
                    curMessage.topMerged = canMerge
                    prevMessage.bottomMerged = canMerge
                }

                withContext(Dispatchers.Main) {
                    channelName = channel.name

                    sortedMessages.clear()
                    sortedMessages.addAll(messageItems)

                    state = State.Loaded
                }
            } catch (t: Throwable) {
                t.printStackTrace()

                withContext(Dispatchers.Main) {
                    state = State.Error
                }
            }
        }

        messageStore.observeChannel(channelId).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = { msg ->
                    val topMessage = sortedMessages.getOrNull(0)
                    val canMerge = canMessagesMerge(msg, topMessage?.message)

                    sortedMessages.getOrNull(0)?.bottomMerged = canMerge
                    sortedMessages.add(0, MessageItem(msg, topMerged = canMerge))
                },
                onUpdate = { msg ->
                    val i = getMessageItemIndex(msg.id)
                        ?: return@fold

                    val topMessage = sortedMessages.getOrNull(i + 1)
                    val bottomMessage = sortedMessages.getOrNull(i - 1)
                    val canMerge = canMessagesMerge(bottomMessage?.message, topMessage?.message)

                    topMessage?.bottomMerged = canMerge
                    bottomMessage?.topMerged = canMerge
                    sortedMessages.getOrNull(i)?.message = msg
                },
                onDelete = { data ->
                    val i = getMessageItemIndex(data.messageId.value)
                        ?: return@fold

                    val topMessage = sortedMessages.getOrNull(i + 1)
                    val bottomMessage = sortedMessages.getOrNull(i - 1)
                    val canMerge = canMessagesMerge(bottomMessage?.message, topMessage?.message)

                    topMessage?.bottomMerged = canMerge
                    bottomMessage?.topMerged = canMerge
                    sortedMessages.removeAt(i)
                },
            )
        }

        reactionStore.observeChannel(channelId).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = { /* onAdd doesn't exist */ },
                onUpdate = { data ->
                    val reactions = getMessageItemIndex(data.messageId)
                        ?.let { sortedMessages.getOrNull(it)?.reactions }
                        ?: return@fold

                    reactions.compute(data.emoji.identifier) { _, state ->
                        val newCount = data.countDiff + (state?.count ?: 0)
                        if (newCount <= 0) return@compute null

                        state?.apply {
                            count = newCount
                            data.meReacted?.let { meReacted = it }
                        } ?: ReactionState(
                            reactionOrder = System.currentTimeMillis(),
                            emoji = data.emoji,
                            count = newCount,
                            meReacted = data.meReacted ?: false,
                        )
                    }
                },
                onDelete = { data ->
                    getMessageItemIndex(data.messageId)
                        ?.let { sortedMessages.getOrNull(it)?.reactions?.clear() }
                },
            )
        }
    }

    fun reactToMessage(messageId: Long, emoji: DomainEmoji) {
        viewModelScope.launch {
            val meReacted = getMessageItemIndex(messageId)
                ?.let { sortedMessages.getOrNull(it)?.reactions?.get(emoji.identifier)?.meReacted }
                ?: false

            if (meReacted) {
                api.removeMeReaction(persistentChannelId, messageId, emoji)
            } else {
                api.addMeReaction(persistentChannelId, messageId, emoji)
            }
        }
    }

    init {
        if (persistentGuildId != 0L && persistentChannelId != 0L) {
            load()
        }

        currentUserStore.observeCurrentUser().collectIn(viewModelScope) { user ->
            currentUserId = user.id
        }
    }

    private fun canMessagesMerge(message: DomainMessage?, prevMessage: DomainMessage?): Boolean {
        return message != null && prevMessage != null
                && message.author.id == prevMessage.author.id
                && prevMessage is DomainMessageRegular
                && message is DomainMessageRegular
                && !message.isReply
                && (message.timestamp - prevMessage.timestamp).inWholeMinutes < 1
                && message.attachments.isEmpty()
                && prevMessage.attachments.isEmpty()
                && message.embeds.isEmpty()
                && prevMessage.embeds.isEmpty()
    }
}
