package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.channel.DomainCategoryChannel
import com.xinto.opencord.domain.channel.DomainChannel
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.store.ChannelStore
import com.xinto.opencord.store.GuildStore
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import com.xinto.opencord.util.collectIn
import com.xinto.opencord.util.getSortedChannels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChannelsViewModel(
    guildId: Long,

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

    val channels = mutableListOf<DomainChannel>()
    var guildName by mutableStateOf("")
        private set
    var guildBannerUrl by mutableStateOf<String?>(null)
        private set
    var guildBoostLevel by mutableStateOf(0)

    var selectedChannelId by mutableStateOf(0L)
        private set
    val collapsedCategories = mutableStateListOf<Long>()

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

    fun getSortedChannels(): Map<DomainCategoryChannel?, List<DomainChannel>> {
        return getSortedChannels(channels)
    }

    init {
        if (persistentChannelId != 0L) {
            selectedChannelId = persistentChannelId
        }
        collapsedCategories.addAll(persistentDataManager.collapsedCategories)

        viewModelScope.launch {
            try {
                guildStore.fetchGuild(guildId)
                channelStore.fetchChannels(guildId)

                state = State.Loaded
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    state = State.Error
                }
            }
        }

        guildStore.observeGuild(persistentGuildId).collectIn(viewModelScope) { event ->
            if (event != null) {
                guildName = event.name
                guildBannerUrl = event.bannerUrl
                guildBoostLevel = event.premiumTier
            } else {
                state = State.Unselected
            }
        }

        channelStore.observeChannels(persistentGuildId).collectIn(viewModelScope) { event ->
            state = State.Loaded
            channels.clear()
            channels.addAll(event)
        }
    }
}
