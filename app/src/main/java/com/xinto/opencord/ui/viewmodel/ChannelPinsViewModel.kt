package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import kotlinx.coroutines.launch

class ChannelPinsViewModel(
    persistentDataManager: PersistentDataManager,
    private val api: DiscordApiService
) : BasePersistenceViewModel(persistentDataManager) {

    sealed interface State {
        object Loading : State
        object Loaded : State
        object Error : State
    }

    var state by mutableStateOf<State>(State.Loading)

    val pins = mutableStateMapOf<Long, DomainMessage>()

    fun load() {
        viewModelScope.launch {
            try {
                state = State.Loading
                val pinnedMessages = api.getChannelPins(persistentChannelId)
                    .associate { it.id.value to it.toDomain() }
                pins.clear()
                pins.putAll(pinnedMessages)
                state = State.Loaded
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}