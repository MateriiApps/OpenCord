package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.store.ChannelStore
import com.xinto.opencord.store.GuildStore
import com.xinto.opencord.store.fold
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import com.xinto.opencord.util.collectIn
import com.xinto.opencord.util.getSortedChannels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChannelsViewModel(
    persistentDataManager: PersistentDataManager,
    private val channelStore: ChannelStore,
    private val guildStore: GuildStore,
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
            state = State.Loading
            withContext(Dispatchers.IO) {
                try {
                    val guild = guildStore.fetchGuild(persistentGuildId) ?: return@withContext
                    val guildChannels = channelStore.fetchChannels(persistentGuildId)
                        .associateBy { it.id }

                    withContext(Dispatchers.Main) {
                        channels.clear()
                        channels.putAll(guildChannels)
                        guildName = guild.name
                        guildBannerUrl = guild.bannerUrl
                        guildBoostLevel = guild.premiumTier
                        state = State.Loaded
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        state = State.Error
                    }
                }
            }
        }

        guildStore.observeGuild(persistentGuildId).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = {
                    guildName = it.name
                    guildBannerUrl = it.bannerUrl
                    guildBoostLevel = it.premiumTier
                },
                onUpdate = {
                    guildName = it.name
                    guildBannerUrl = it.bannerUrl
                    guildBoostLevel = it.premiumTier
                },
                onRemove = {
                    state = State.Unselected
                },
            )
        }

        channelStore.observeChannels(persistentGuildId).collectIn(viewModelScope) { event ->
            state = State.Loaded
            event.fold(
                onAdd = { channels[it.id] = it },
                onUpdate = { channels[it.id] = it },
                onRemove = { channels.remove(it) },
            )
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
    }
}
