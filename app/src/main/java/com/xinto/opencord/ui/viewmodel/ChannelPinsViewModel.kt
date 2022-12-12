package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.store.MessageStore
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import kotlinx.coroutines.launch

class ChannelPinsViewModel(
    channelId: Long,

    persistentDataManager: PersistentDataManager,
    private val messageStore: MessageStore,
) : BasePersistenceViewModel(persistentDataManager) {
    sealed interface State {
        object Loading : State
        object Loaded : State
        object Error : State
    }

    var state by mutableStateOf<State>(State.Loading)

    val pins = mutableStateMapOf<Long, DomainMessage>()

    init {
        viewModelScope.launch {
            try {
                state = State.Loading

                val messages = messageStore.fetchPinnedMessages(channelId)
                pins.clear()
                pins.putAll(messages.map { it.id to it })

                state = State.Loaded
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
