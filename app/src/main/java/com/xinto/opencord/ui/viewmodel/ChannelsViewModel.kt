package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.channel.DomainCategoryChannel
import com.xinto.opencord.domain.channel.DomainChannel
import com.xinto.opencord.domain.channel.DomainUnreadState
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.store.*
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import com.xinto.opencord.util.collectIn
import com.xinto.opencord.util.getSortedChannels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChannelsViewModel(
    persistentDataManager: PersistentDataManager,
    private val channelStore: ChannelStore,
    private val guildStore: GuildStore,
    private val lastMessageStore: LastMessageStore,
    private val unreadStore: UnreadStore,
) : BasePersistenceViewModel(persistentDataManager) {
    sealed interface State {
        object Unselected : State
        object Loading : State
        object Loaded : State
        object Error : State
    }

    var state by mutableStateOf<State>(State.Unselected)
        private set

    var selectedChannelId by mutableStateOf(0L)
        private set

    val channels = mutableStateMapOf<Long, DomainChannel>()
    val unreadStates = mutableStateMapOf<Long, DomainUnreadState>()
    val collapsedCategories = mutableStateListOf<Long>()
    val lastMessageIds = mutableStateMapOf<Long, Long>()

    var guildName by mutableStateOf("")
        private set
    var guildBannerUrl by mutableStateOf<String?>(null)
        private set
    var guildBoostLevel by mutableStateOf(0)
        private set

    fun load() {
        if (persistentGuildId <= 0L) return

        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch {
            state = State.Loading
            withContext(Dispatchers.IO) {
                try {
                    val guild = guildStore.fetchGuild(persistentGuildId) ?: return@withContext
                    val guildChannels = channelStore.fetchChannels(persistentGuildId)
                        .associateBy { it.id }
                    val guildChannelIds = guildChannels.keys.toList()
                    val states = guildChannels.keys
                        .mapNotNull { unreadStore.getChannel(it) }
                        .associateBy { it.channelId }
                    val lastMessages = guildChannels.keys.mapNotNull {
                        it to (lastMessageStore.getLastMessageId(it) ?: return@mapNotNull null)
                    }

                    unreadStore.observeChannels(guildChannelIds).collectIn(viewModelScope) { event ->
                        event.fold(
                            onAdd = { unreadStates[it.channelId] = it },
                            onUpdate = { },
                            onDelete = { unreadStates.remove(it) },
                        )
                    }

                    lastMessageStore.observeChannels(guildChannelIds).collectIn(viewModelScope) { event ->
                        event.fold(
                            onAdd = { (id, messageId) -> lastMessageIds[id] = messageId },
                            onUpdate = { },
                            onDelete = { id -> lastMessageIds.remove(id) },
                        )
                    }

                    withContext(Dispatchers.Main) {
                        channels.clear()
                        channels.putAll(guildChannels)

                        unreadStates.clear()
                        unreadStates.putAll(states)

                        lastMessageIds.clear()
                        lastMessageIds.putAll(lastMessages)

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
                onDelete = {
                    state = State.Unselected
                },
            )
        }

        channelStore.observeChannels(persistentGuildId).collectIn(viewModelScope) { event ->
            state = State.Loaded
            event.fold(
                onAdd = { channels[it.id] = it },
                onUpdate = { channels[it.id] = it },
                onDelete = {
                    channels.remove(it)
                    lastMessageIds.remove(it)
                },
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

    fun getSortedChannels(): Map<DomainCategoryChannel?, List<DomainChannel>> {
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
