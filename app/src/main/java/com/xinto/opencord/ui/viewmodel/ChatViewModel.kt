package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.body.MessageBody
import kotlinx.coroutines.launch

class ChatViewModel(
    gateway: DiscordGateway,
    private val repository: DiscordApiRepository,
    private val persistentDataManager: PersistentDataManager
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

    fun load() {
        viewModelScope.launch {
            try {
                state = State.Loading
                val currentChannelId = persistentDataManager.currentChannelId
                val channelMessages = repository.getChannelMessages(currentChannelId)
                val channel = repository.getChannel(currentChannelId)
                messages.clear()
                messages.addAll(channelMessages)
                channelName = channel.name
                state = State.Loaded
            } catch (e: Exception) {
                state = State.Error
                e.printStackTrace()
            }
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            sendEnabled = false
            val message = userMessage
            userMessage = ""
            repository.postChannelMessage(
                channelId = persistentDataManager.currentChannelId,
                MessageBody(
                    content = message
                )
            )
            sendEnabled = true
        }
    }

    fun updateMessage(message: String) {
        userMessage = message
    }

    init {
        gateway.onEvent<MessageCreateEvent>(
            filterPredicate = { it.data.channelId == persistentDataManager.currentChannelId }
        ) {
            val domainData = it.data.toDomain()
            messages.add(0, domainData)
        }

        if (persistentDataManager.currentChannelId != 0L) {
            load()
        }
    }

}