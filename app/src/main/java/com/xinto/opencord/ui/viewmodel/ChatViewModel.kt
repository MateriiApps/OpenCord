package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.domain.model.DomainMessageRegular
import com.xinto.opencord.domain.model.merge
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.domain.store.ListEvent
import com.xinto.opencord.domain.store.MessageStore
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.event.MessageDeleteEvent
import com.xinto.opencord.gateway.event.MessageUpdateEvent
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import com.xinto.opencord.util.throttle
import com.xinto.partialgen.getOrNull
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatViewModel(
    private val channelId: Long,

    private val messageStore: MessageStore,
    private val repository: DiscordApiRepository
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

    private val job = viewModelScope.launch {
        messageStore.observe(channelId).collect {
            when (it) {
                is ListEvent.Add -> {
                    messages[it.data.id] = it.data
                }
                is ListEvent.Update -> {
                    if (messages[it.data.id] != null) {
                        messages[it.data.id] = it.data
                    }
                }
                is ListEvent.Remove -> {
                    messages.remove(it.data.id)
                }
            }
        }
    }

    val startTyping = throttle(9500, viewModelScope) {
        repository.startTyping(channelId)
    }

    fun sendMessage() {
        viewModelScope.launch {
            sendEnabled = false
            val message = userMessage
            userMessage = ""
            repository.postChannelMessage(
                channelId = channelId,
                MessageBody(
                    content = message
                )
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
        job.cancel()
    }

}
