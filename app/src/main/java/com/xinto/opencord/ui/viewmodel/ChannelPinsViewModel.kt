package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.store.ChannelStore
import com.xinto.opencord.store.MessageStore
import com.xinto.opencord.store.fold
import com.xinto.opencord.ui.navigation.PinsScreenData
import com.xinto.opencord.util.collectIn
import kotlinx.coroutines.launch

@Stable
class ChannelPinsViewModel(
    val data: PinsScreenData,
    messageStore: MessageStore,
    channelStore: ChannelStore,
) : ViewModel() {
    sealed interface State {
        object Loading : State
        object Loaded : State
        object Error : State
    }

    var state by mutableStateOf<State>(State.Loading)

    val messages = mutableStateMapOf<Long, DomainMessage>()
    var channelName by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            try {
                state = State.Loading

                val channel = channelStore.fetchChannel(data.channelId)
                    ?: error("Channel not cached for pins screen")

                channelName = channel.name

                val fetchedMessages = messageStore.fetchPinnedMessages(data.channelId)
                messages.putAll(fetchedMessages.map { it.id to it })

                state = State.Loaded
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        channelStore.observeChannel(data.channelId).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = {},
                onUpdate = { channelName = it.name },
                onDelete = {}, // Should this close the screen?
            )
        }

        messageStore.observeChannel(data.channelId).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = {},
                onUpdate = { messages.replace(it.id, it) },
                onDelete = { messages.remove(it.messageId.value) },
            )
        }
    }
}
