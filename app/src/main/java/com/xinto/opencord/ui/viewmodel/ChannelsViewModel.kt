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

    @Stable
    class ChannelItemData(
        channel: DomainChannel,
        unreadState: DomainUnreadState?,
        lastMessageId: Long?,
    ) {
        var channel by mutableStateOf(channel)
        var unreadState by mutableStateOf(unreadState)
        var lastMessageId by mutableStateOf(lastMessageId)

        val isUnread: Boolean
            get() = (lastMessageId ?: 0) > (unreadState?.lastMessageId ?: 0)
    }

    @Stable
    class CategoryItemData(
        channel: DomainCategoryChannel,
        collapsed: Boolean,
        subChannels: List<ChannelItemData>?,
    ) {
        var channel by mutableStateOf(channel)
        var collapsed by mutableStateOf(collapsed)
        var subChannels = mutableStateMapOf<Long, ChannelItemData>()
            .apply { subChannels?.also { putAll(subChannels.associateBy { it.channel.id }) } }
    }

    var state by mutableStateOf<State>(State.Unselected)
        private set

    var selectedChannelId by mutableStateOf(0L)
        private set

    var guildName by mutableStateOf("")
        private set
    var guildBannerUrl by mutableStateOf<String?>(null)
        private set
    var guildBoostLevel by mutableStateOf(0)
        private set

    val categoryChannels = mutableStateMapOf<Long, CategoryItemData>()
    val noCategoryChannels = mutableStateMapOf<Long, ChannelItemData>()

    // Dual reference to all channel items for events updating state
    private val allChannelItems = mutableMapOf<Long, ChannelItemData>()

    fun load() {
        if (persistentGuildId <= 0L) return

        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch {
            state = State.Loading
            withContext(Dispatchers.IO) {
                try {
                    val guild = guildStore.fetchGuild(persistentGuildId)
                        ?: return@withContext

                    val channels = channelStore.fetchChannels(persistentGuildId)

                    val (categoryItems, channelItems) = channels
                        .partition { it is DomainCategoryChannel }
                        .let { (newCategories, newChannels) ->
                            val channelItems = newChannels.associate {
                                it.id to ChannelItemData(
                                    channel = it,
                                    unreadState = unreadStore.getChannel(it.id),
                                    lastMessageId = lastMessageStore.getLastMessageId(it.id),
                                )
                            }

                            val categoryItems = newCategories.associate { category ->
                                category.id to CategoryItemData(
                                    channel = category as DomainCategoryChannel,
                                    collapsed = persistentCollapsedCategories.contains(category.id),
                                    subChannels = channelItems.values.filter {
                                        if (it.channel is DomainCategoryChannel)
                                            false
                                        else {
                                            it.channel.parentId == category.id
                                        }
                                    },
                                )
                            }

                            categoryItems to channelItems
                        }

                    val noCategoryItems = channelItems
                        .filterValues { it.channel.parentId == null }

                    val channelItemIds = channelItems.keys.toList()

                    unreadStore.observeChannels(channelItemIds).collectIn(viewModelScope) { event ->
                        event.fold(
                            onAdd = {
                                allChannelItems[it.channelId]?.unreadState = it
                            },
                            onUpdate = { },
                            onDelete = {
                                val categoryId = allChannelItems[it]?.channel?.parentId

                                if (categoryId != null) {
                                    allChannelItems.remove(it)
                                    categoryChannels[categoryId]?.subChannels?.remove(it)
                                }
                            },
                        )
                    }

                    lastMessageStore.observeChannels(channelItemIds).collectIn(viewModelScope) { event ->
                        event.fold(
                            onAdd = { (channelId, messageId) ->
                                allChannelItems[channelId]?.lastMessageId = messageId
                            },
                            onUpdate = { },
                            onDelete = { /* Handled by UnreadStore listener */ },
                        )
                    }

                    withContext(Dispatchers.Main) {
                        allChannelItems.clear()
                        allChannelItems.putAll(channelItems)

                        categoryChannels.clear()
                        categoryChannels.putAll(categoryItems)

                        noCategoryChannels.clear()
                        noCategoryChannels.putAll(noCategoryItems)

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
                onAdd = {
                    if (it is DomainCategoryChannel) {
                        categoryChannels[it.id] = CategoryItemData(
                            channel = it,
                            collapsed = false,
                            subChannels = emptyList(),
                        )
                    } else {
                        val item = ChannelItemData(
                            channel = it,
                            unreadState = null,
                            lastMessageId = null,
                        )
                        allChannelItems[it.id] = item
                    }
                },
                onUpdate = { allChannelItems[it.id]?.channel = it },
                onDelete = {
                    val categoryId = allChannelItems[it]?.channel?.parentId

                    if (categoryId != null) {
                        allChannelItems.remove(it)
                        categoryChannels[categoryId]?.subChannels?.remove(it)
                    }
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
            removePersistentCollapseCategory(categoryId)
        else {
            addPersistentCollapseCategory(categoryId)
        }

        categoryChannels[categoryId]?.apply { collapsed = !collapsed }
    }

    init {
        if (persistentGuildId != 0L) {
            load()
        }
        if (persistentChannelId != 0L) {
            selectedChannelId = persistentChannelId
        }
    }
}
