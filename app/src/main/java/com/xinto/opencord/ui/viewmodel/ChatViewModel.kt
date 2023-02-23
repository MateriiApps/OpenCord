package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.emoji.DomainEmoji
import com.xinto.opencord.domain.emoji.DomainEmojiIdentifier
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.store.ChannelStore
import com.xinto.opencord.store.MessageStore
import com.xinto.opencord.store.ReactionStore
import com.xinto.opencord.store.fold
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import com.xinto.opencord.util.collectIn
import com.xinto.opencord.util.throttle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val messageStore: MessageStore,
    private val reactionStore: ReactionStore,
    private val channelStore: ChannelStore,
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

    var state by mutableStateOf<State>(State.Unselected)
        private set

    val messages = mutableStateMapOf<Long, DomainMessage>()
    val reactions = mutableStateMapOf<Long, SnapshotStateMap<DomainEmojiIdentifier, ReactionState>>()

    var channelName by mutableStateOf("")
        private set
    var userMessage by mutableStateOf("")
        private set
    var sendEnabled by mutableStateOf(true)
        private set
    var currentUserId by mutableStateOf<Long?>(null)
        private set

    val startTyping = throttle(9500, viewModelScope) {
        api.startTyping(persistentDataManager.persistentChannelId)
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
                val channelMessages = messageStore.fetchMessages(channelId)
                val channelReactions = channelMessages.associate { msg ->
                    val messageReactions = reactionStore.getReactions(msg.id).asSequence().mapIndexed { i, reaction ->
                        reaction.emoji.identifier to ReactionState(
                            reactionOrder = i.toLong(),
                            emoji = reaction.emoji,
                            count = reaction.count,
                            meReacted = reaction.meReacted,
                        )
                    }

                    msg.id to mutableStateMapOf<DomainEmojiIdentifier, ReactionState>()
                        .apply { putAll(messageReactions) }
                }

                withContext(Dispatchers.Main) {
                    channelName = channel.name

                    messages.clear()
                    messages.putAll(channelMessages.associateBy { it.id })

                    reactions.clear()
                    reactions.putAll(channelReactions)

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
                onAdd = { messages[it.id] = it },
                onUpdate = { messages[it.id] = it },
                onDelete = { messages.remove(it.messageId.value) },
            )
        }

        reactionStore.observeChannel(channelId).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = {},
                onUpdate = { data ->
                    reactions
                        .computeIfAbsent(data.messageId) { mutableStateMapOf() }
                        .compute(data.emoji.identifier) { _, state ->
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
                onDelete = {
                    reactions.remove(it.messageId)
                },
            )
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            sendEnabled = false
            val message = userMessage
            userMessage = ""
            api.postChannelMessage(
                channelId = persistentDataManager.persistentChannelId,
                MessageBody(
                    content = message,
                ),
            )
            sendEnabled = true
        }
    }

    fun updateMessage(message: String) {
        userMessage = message
        startTyping()
    }

    fun getSortedMessages(): List<DomainMessage> {
        return messages.values.sortedByDescending { it.id }
    }

    init {
        if (persistentGuildId != 0L && persistentChannelId != 0L) {
            load()
        }
    }
}
