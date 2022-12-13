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
import com.xinto.opencord.util.collectIn
import com.xinto.opencord.util.throttle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChatViewModel(
    channelId: Long,

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

    val messages = mutableStateListOf<DomainMessage>()
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

    override fun onCleared() {
        job?.cancel()
    }

    init {
        viewModelScope.launch {
            state = try {
                State.Loading

                channelStore.fetchChannel(channelId)
                messageStore.fetchMessages(channelId)

                State.Loaded
            } catch (t: Throwable) {
                t.printStackTrace()
                State.Error
            }
        }

        job = messageStore
            .observeMessages(channelId)
            .collectIn(viewModelScope) { event ->
                messages.clear()
                messages.addAll(event)
            }

        channelStore
            .observeChannel(channelId)
            .collectIn(viewModelScope) { event ->
                if (event != null) {
                    channelName = event.name
                } else {
                    state = State.Unselected
                }
            }
    }
}
