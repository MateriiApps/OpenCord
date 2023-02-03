package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.store.ChannelStore
import com.xinto.opencord.store.MessageStore
import com.xinto.opencord.store.fold
import com.xinto.opencord.util.collectIn
import com.xinto.opencord.util.throttle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatViewModel(
    private val messageStore: MessageStore,
    private val channelStore: ChannelStore,
    private val api: DiscordApiService,
    private val persistentDataManager: PersistentDataManager,
) : ViewModel() {
    sealed interface State {
        object Unselected : State
        object Loading : State
        object Loaded : State
        object Error : State
    }

    var state by mutableStateOf<State>(State.Unselected)
        private set

    val messages = mutableStateMapOf<Long, DomainMessage>()
    var channelName by mutableStateOf("")
        private set
    var userMessage by mutableStateOf("")
        private set
    var sendEnabled by mutableStateOf(true)
        private set
    var currentUserId by mutableStateOf<Long?>(null)
        private set

    private var job: Job? = null

    val startTyping = throttle(9500, viewModelScope) {
        api.startTyping(persistentDataManager.persistentChannelId)
    }

    fun load() {
        viewModelScope.launch {
            state = State.Loading

            try {
                val channel = channelStore.fetchChannel(persistentDataManager.persistentChannelId)
                    ?: return@launch
                val channelMessages =
                    messageStore.fetchMessages(persistentDataManager.persistentChannelId)

                channelName = channel.name
                messages.clear()
                messages.putAll(channelMessages.associateBy { it.id })
                state = State.Loaded
            } catch (t: Throwable) {
                t.printStackTrace()
                state = State.Error
            }
        }

        job?.cancel()
        job = messageStore
            .observeChannel(persistentDataManager.persistentChannelId)
            .collectIn(viewModelScope) { event ->
                event.fold(
                    onAdd = { messages[it.id] = it },
                    onUpdate = { messages[it.id] = it },
                    onDelete = { messages.remove(it.messageId.value) },
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

    override fun onCleared() {
        job?.cancel()
    }
}
