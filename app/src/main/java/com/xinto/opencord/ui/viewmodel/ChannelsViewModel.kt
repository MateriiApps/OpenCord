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
import com.xinto.opencord.gateway.event.GuildUpdateEvent
import com.xinto.opencord.gateway.onEvent
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

    val channels = mutableStateMapOf<Long, DomainChannel>()
    var guildName by mutableStateOf("")
        private set
    var guildBannerUrl by mutableStateOf<String?>(null)
        private set
    var guildBoostLevel by mutableStateOf(0)

    var selectedChannelId by mutableStateOf(0L)
        private set
    val collapsedCategories = mutableStateListOf<Long>()

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

    fun selectChannel(channelId: Long) {
        selectedChannelId = channelId
        persistentChannelId = channelId
    }

    fun toggleCategory(categoryId: Long) {
        if (persistentCollapsedCategories.contains(categoryId))
            addPersistentCollapseCategory(categoryId)
        else {
            removePersistentCollapseCategory(categoryId)
        }

        if (!collapsedCategories.remove(categoryId))
            collapsedCategories.add(categoryId)
    }

    fun getSortedChannels(): Map<DomainChannel.Category?, List<DomainChannel>> {
        return getSortedChannels(channels.values)
    }

    init {
        if (persistentGuildId != 0L) {
            load()
        }
        if (persistentChannelId != 0L) {
            selectedChannelId = persistentChannelId
        }
        collapsedCategories.addAll(persistentDataManager.collapsedCategories)

        gateway.onEvent<GuildUpdateEvent>({ it.data.id.value == persistentGuildId }) {
            guildName = it.data.name
            guildBannerUrl = it.data.banner
            guildBoostLevel = it.data.premiumTier
        }

        gateway.onEvent<ChannelCreateEvent>({ it.data.guildId?.value == persistentGuildId }) {
            val domainData = it.data.toDomain()
            channels[domainData.id] = domainData
        }
        gateway.onEvent<ChannelUpdateEvent>({ it.data.guildId?.value == persistentGuildId }) {
            val domainData = it.data.toDomain()
            channels[domainData.id] = domainData
        }
        gateway.onEvent<ChannelDeleteEvent>({ it.data.guildId?.value == persistentGuildId }) {
            channels.remove(it.data.id.value)
        }
    }
}
