package com.xinto.opencord.ui.screens.home.panels.chat

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.emoji.DomainEmoji
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.DomainMessageRegular
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.store.*
import com.xinto.opencord.ui.screens.home.panels.chat.model.MessageItem
import com.xinto.opencord.ui.screens.home.panels.chat.model.ReactionState
import com.xinto.opencord.util.collectIn
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@Stable
class HomeChatPanelViewModel(
    private val messageStore: MessageStore,
    private val reactionStore: ReactionStore,
    private val channelStore: ChannelStore,
    private val currentUserStore: CurrentUserStore,
    private val persistentDataStore: PersistentDataStore,
    private val api: DiscordApiService,
) : ViewModel() {

    var state by mutableStateOf<HomeChatPanelState>(HomeChatPanelState.Unselected)
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

    fun reactToMessage(messageId: Long, emoji: DomainEmoji) {
        viewModelScope.launch {
            val meReacted = getMessageItemIndex(messageId)
                ?.let { sortedMessages.getOrNull(it)?.reactions?.get(emoji.identifier)?.meReacted }
                ?: false

            val channelId = persistentDataStore.observeCurrentChannel().last()
            if (meReacted) {
                api.removeMeReaction(channelId, messageId, emoji)
            } else {
                api.addMeReaction(channelId, messageId, emoji)
            }
        }
    }

    init {
        persistentDataStore.observeCurrentChannel()
            .collectIn(viewModelScope) { channelId ->
                if (channelId == 0L) {
                    state = HomeChatPanelState.Unselected
                    return@collectIn
                }

                state = HomeChatPanelState.Loading

                val channel = channelStore.fetchChannel(channelId)
                if (channel == null) {
                    state = HomeChatPanelState.Error
                    return@collectIn
                }

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
                        currentUserId = currentUserId
                    )
                }

                for (i in 0 until (messageItems.size - 1)) {
                    val curMessage = messageItems[i]
                    val prevMessage = messageItems[i + 1]

                    val canMerge = canMessagesMerge(curMessage.message, prevMessage.message)
                    curMessage.topMerged = canMerge
                    prevMessage.bottomMerged = canMerge
                }

                channelName = channel.name

                sortedMessages.clear()
                sortedMessages.addAll(messageItems)

                state = HomeChatPanelState.Loaded
            }

        persistentDataStore.observeCurrentChannel()
            .mapNotNull { channelId ->
                if (channelId == 0L) null else messageStore.observeChannel(channelId)
            }.flattenMerge().collectIn(viewModelScope) { event ->
                event.fold(
                    onAdd = { msg ->
                        val topMessage = sortedMessages.getOrNull(0)
                        val canMerge = canMessagesMerge(msg, topMessage?.message)

                        sortedMessages.getOrNull(0)?.bottomMerged = canMerge
                        sortedMessages.add(0, MessageItem(msg, topMerged = canMerge, currentUserId = currentUserId))
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

        persistentDataStore.observeCurrentChannel()
            .mapNotNull { channelId ->
                if (channelId == 0L) null else reactionStore.observeChannel(channelId)
            }.flattenMerge().collectIn(viewModelScope) { event ->
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
