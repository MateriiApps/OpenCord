package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import com.xinto.opencord.util.getSortedChannels
import kotlinx.coroutines.launch

class ChannelsViewModel(
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

    val channels = mutableStateMapOf<DomainChannel.Category?, List<DomainChannel>>()
    var guildName by mutableStateOf("")
        private set
    var guildBannerUrl by mutableStateOf<String?>(null)
        private set
    var selectedChannelId by mutableStateOf(0UL)
        private set

    fun load() {
        viewModelScope.launch {
            try {
                state = State.Loading
                val guildChannels = repository.getGuildChannels(persistentGuildId)
                val guild = repository.getGuild(persistentGuildId)
                channels.clear()
                channels.putAll(getSortedChannels(guildChannels))
                guildName = guild.name
                guildBannerUrl = guild.bannerUrl
                state = State.Loaded
            } catch (e: Exception) {
                state = State.Error
                e.printStackTrace()
            }
        }
    }

    fun selectChannel(channelId: ULong) {
        selectedChannelId = channelId
        persistentChannelId = channelId
    }

    init {
        if (persistentGuildId != 0UL) {
            load()
        }
        if (persistentChannelId != 0UL) {
            selectedChannelId = persistentDataManager.persistentChannelId
        }
    }

}