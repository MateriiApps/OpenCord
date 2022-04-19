package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.event.MessageDeleteEvent
import com.xinto.opencord.gateway.event.MessageUpdateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import kotlinx.coroutines.launch

class ChatViewModel(
    gateway: DiscordGateway,
    persistentDataManager: PersistentDataManager,
    private val repository: DiscordApiRepository
) : BasePersistenceViewModel(persistentDataManager) {

    sealed interface State {
        object Unselected : State
        object Loading : State
        object Loaded : State
        object Error : State
    }

    var state by mutableStateOf<State>(State.Unselected)
        private set

    val messages = mutableStateMapOf<ULong, DomainMessage>()
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
                val channelMessages = repository.getChannelMessages(persistentChannelId)
                val channel = repository.getChannel(persistentChannelId)
                messages.clear()
                messages.putAll(channelMessages)
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
                channelId = persistentChannelId,
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
            filterPredicate = { it.data.channelId.value == persistentChannelId }
        ) { event ->
            val domainData = event.data.toDomain()
            messages[domainData.id] = domainData
        }

        gateway.onEvent<MessageUpdateEvent>(
            filterPredicate = { it.data.channelId.value == persistentChannelId }
        ) { event ->
            val domainData = event.data.toDomain()
            messages[domainData.id] = domainData
        }

        gateway.onEvent<MessageDeleteEvent>(
            filterPredicate = { it.data.channelId.value == persistentChannelId }
        ) { event ->
            messages.remove(event.data.messageId.value)
        }

        if (persistentChannelId != 0UL) {
            load()
        }
    }

}