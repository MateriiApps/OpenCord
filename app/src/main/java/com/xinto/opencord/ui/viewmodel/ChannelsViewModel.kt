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
    var guildBoostLevel by mutableStateOf(0)

    var selectedChannelId by mutableStateOf(0UL)
        private set
    val collapsedCategories = mutableStateListOf<ULong>()

    fun load() {
        viewModelScope.launch {
            try {
                state = State.Loading
                val guildChannels = repository.getGuildChannels(persistentGuildId)
                val guild = repository.getGuild(persistentGuildId)
                channels.clear()
                channels.putAll(guildChannels)
                guildName = guild.name
                guildBannerUrl = guild.bannerUrl
                guildBoostLevel = guild.premiumTier
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

    fun toggleCategory(categoryId: ULong) {
        if (persistentCollapsedCategories.contains(categoryId)) {
            addPersistentCollapseCategory(categoryId)
        } else {
            removePersistentCollapseCategory(categoryId)
        }

        if (!collapsedCategories.remove(categoryId))
            collapsedCategories.add(categoryId)
    }

    init {
        if (persistentGuildId != 0UL) {
            load()
        }
        if (persistentChannelId != 0UL) {
            selectedChannelId = persistentChannelId
        }
        collapsedCategories.addAll(persistentDataManager.collapsedCategories)
        gateway.onEvent<ChannelCreateEvent>(
            filterPredicate = { it.data.guildId?.value == persistentGuildId }
        ) {
            val domainData = it.data.toDomain()
            channels[domainData.id] = domainData
        }
        gateway.onEvent<ChannelUpdateEvent>(
            filterPredicate = { it.data.guildId?.value == persistentGuildId }
        ) {
            val domainData = it.data.toDomain()
            channels[domainData.id] = domainData
        }
        gateway.onEvent<ChannelDeleteEvent>(
            filterPredicate = { it.data.guildId?.value == persistentGuildId }
        ) {
            val domainData = it.data.toDomain()
            channels.remove(domainData.id)
        }
    }
}
