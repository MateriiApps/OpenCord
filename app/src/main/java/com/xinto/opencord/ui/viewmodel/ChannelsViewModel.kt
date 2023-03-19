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
import kotlinx.coroutines.*


@Stable
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
        var unreadListenerJob: Job?,
        var lastMessageListenerJob: Job?,
    ) {
        var channel by mutableStateOf(channel)
        var unreadState by mutableStateOf(unreadState)
        var lastMessageId by mutableStateOf(lastMessageId)

        val isUnread: Boolean
            get() = (lastMessageId ?: 0) > (unreadState?.lastMessageId ?: 0)

        fun cancelJobs() {
            unreadListenerJob?.cancel()
            lastMessageListenerJob?.cancel()
        }
    }

    @Stable
    class CategoryItemData(
        channel: DomainCategoryChannel,
        collapsed: Boolean,
        subChannels: List<ChannelItemData>?,
    ) {
        var channel by mutableStateOf(channel)
        var collapsed by mutableStateOf(collapsed)
        var channels = mutableStateMapOf<Long, ChannelItemData>()

        val channelsSorted by derivedStateOf {
            channels.values.sortedWith { a, b -> a.channel compareTo b.channel }
        }

        init {
            if (subChannels != null) {
                channels.putAll(subChannels.associateBy { it.channel.id })
            }
        }
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

                    replaceChannels(channelStore.fetchChannels(persistentGuildId))

                    withContext(Dispatchers.Main) {
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

        channelStore.observeChannelsReplace(persistentGuildId).collectIn(viewModelScope) { channels ->
            replaceChannels(channels)
        }

        channelStore.observeChannels(persistentGuildId).collectIn(viewModelScope) { event ->
            state = State.Loaded
            event.fold(
                onAdd = { channel ->
                    if (channel is DomainCategoryChannel) {
                        categoryChannels[channel.id] = CategoryItemData(
                            channel = channel,
                            collapsed = false,
                            subChannels = null,
                        )
                    } else {
                        val item = makeAliveChannelItem(channel)

                        // Remove from old category
                        if (allChannelItems[channel.id]?.channel?.parentId != null) {
                            categoryChannels[channel.id]?.channels?.remove(channel.id)
                        }

                        allChannelItems[channel.id] = item
                        channel.parentId?.let { categoryChannels[it]?.channels?.set(it, item) }
                    }
                },
                onUpdate = { channel ->
                    if (channel is DomainCategoryChannel) {
                        categoryChannels.compute(channel.id) { _, categoryItem ->
                            categoryItem?.apply {
                                this.channel = channel
                            } ?: CategoryItemData(
                                channel = channel,
                                collapsed = false,
                                subChannels = allChannelItems.values.filter { channelItem ->
                                    if (channelItem.channel is DomainCategoryChannel)
                                        false
                                    else {
                                        channelItem.channel.parentId == channel.id
                                    }
                                },
                            )
                        }
                    } else {
                        allChannelItems[channel.id]?.channel = channel
                    }
                },
                onDelete = {
                    val categoryId = allChannelItems[it]?.channel?.parentId

                    if (categoryId != null) {
                        allChannelItems.remove(it)
                        categoryChannels[categoryId]?.channels?.remove(it)
                    }
                },
            )
        }
    }

    private suspend fun makeAliveChannelItem(channel: DomainChannel): ChannelItemData {
        if (channel is DomainCategoryChannel) {
            error("cannot make channel item from category channel")
        }

        val item = ChannelItemData(
            channel = channel,
            unreadState = unreadStore.getChannel(channel.id),
            lastMessageId = lastMessageStore.getLastMessageId(channel.id),
            unreadListenerJob = null,
            lastMessageListenerJob = null,
        )

        item.unreadListenerJob = unreadStore.observeChannel(channel.id).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = {
                    item.unreadState = it
                },
                onUpdate = { },
                onDelete = {
                    val categoryId = item.channel.parentId

                    if (categoryId != null) {
                        allChannelItems.remove(it)
                        categoryChannels[categoryId]?.channels?.remove(it)
                        item.cancelJobs()
                    }
                },
            )
        }

        item.lastMessageListenerJob = lastMessageStore.observeChannel(channel.id).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = { (_, messageId) ->
                    item.lastMessageId = messageId
                },
                onUpdate = { },
                onDelete = { /* Handled by UnreadStore listener */ },
            )
        }

        return item
    }

    private suspend fun replaceChannels(channels: List<DomainChannel>) {
        val (categoryItems, channelItems) = channels
            .partition { it is DomainCategoryChannel }
            .let { (newCategories, newChannels) ->
                val channelItems = newChannels.associate {
                    it.id to makeAliveChannelItem(it)
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

        withContext(Dispatchers.Main) {
            allChannelItems.values.forEach { it.cancelJobs() }
            allChannelItems.clear()
            allChannelItems.putAll(channelItems)

            categoryChannels.clear()
            categoryChannels.putAll(categoryItems)

            noCategoryChannels.clear()
            noCategoryChannels.putAll(noCategoryItems)
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
