package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.util.getSortedChannels
import kotlinx.coroutines.launch

class ChannelsViewModel(
    gateway: DiscordGateway,
    private val repository: DiscordApiRepository,
    private val persistentDataManager: PersistentDataManager
) : ViewModel() {

    sealed interface State {
        object Loading : State
        object Loaded : State
        object Error : State
    }

    var state by mutableStateOf<State>(State.Loading)
        private set

    val channels = mutableStateMapOf<DomainChannel.Category?, List<DomainChannel>>()
    var selectedChannelId by mutableStateOf(0L)
        private set

    fun load() {
        viewModelScope.launch {
            try {
                state = State.Loading
                val guildChannels = repository.getGuildChannels(persistentDataManager.currentGuildId)
                channels.clear()
                channels.putAll(getSortedChannels(guildChannels))
                state = State.Loaded
            } catch (e: Exception) {
                state = State.Error
                e.printStackTrace()
            }
        }
    }

    fun selectChannel(channelId: Long) {
        selectedChannelId = channelId
        persistentDataManager.currentChannelId = channelId
    }

    init {
        if (persistentDataManager.currentGuildId != 0L) {
            load()
        }
        if (persistentDataManager.currentChannelId != 0L) {
            selectedChannelId = persistentDataManager.currentChannelId
        }
    }

}