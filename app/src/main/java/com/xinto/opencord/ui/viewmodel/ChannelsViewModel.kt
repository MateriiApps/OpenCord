package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.ChannelCreateEvent
import com.xinto.opencord.gateway.event.ChannelDeleteEvent
import com.xinto.opencord.gateway.event.ChannelUpdateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
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

    val channels = mutableStateMapOf<ULong, DomainChannel>()
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
                val guildId = getPersistentGuildId()
                val guildChannels = repository.getGuildChannels(guildId)
                val guild = repository.getGuild(guildId)
                channels.clear()
                channels.putAll(guildChannels)
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
        viewModelScope.launch {
            selectedChannelId = channelId
            setPersistentChannelId(channelId)
        }
    }

    init {
        gateway.onEvent<ChannelCreateEvent>(
            filterPredicate = { it.data.guildId?.value == getPersistentGuildId() }
        ) {
            val domainData = it.data.toDomain()
            channels[domainData.id] = domainData
        }
        gateway.onEvent<ChannelUpdateEvent>(
            filterPredicate = { it.data.guildId?.value == getPersistentGuildId() }
        ) {
            val domainData = it.data.toDomain()
            channels[domainData.id] = domainData
        }
        gateway.onEvent<ChannelDeleteEvent>(
            filterPredicate = { it.data.guildId?.value == getPersistentGuildId() }
        ) {
            val domainData = it.data.toDomain()
            channels.remove(domainData.id)
        }
        viewModelScope.launch {
            if (getPersistentGuildId() != 0UL) {
                load()
            }

            val persistentChannelId = getPersistentChannelId()
            if (persistentChannelId != 0UL) {
                selectedChannelId = persistentChannelId
            }
        }
    }

}