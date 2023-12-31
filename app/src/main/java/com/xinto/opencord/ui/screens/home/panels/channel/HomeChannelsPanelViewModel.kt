package com.xinto.opencord.ui.screens.home.panels.channel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.channel.DomainCategoryChannel
import com.xinto.opencord.domain.channel.DomainChannel
import com.xinto.opencord.store.*
import com.xinto.opencord.ui.screens.home.panels.channel.model.CategoryItemData
import com.xinto.opencord.ui.screens.home.panels.channel.model.ChannelItemData
import com.xinto.opencord.util.collectIn
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.mapNotNull

@OptIn(FlowPreview::class)
@Stable
class HomeChannelsPanelViewModel(
    private val channelStore: ChannelStore,
    private val guildStore: GuildStore,
    private val lastMessageStore: LastMessageStore,
    private val unreadStore: UnreadStore,
    private val persistentDataStore: PersistentDataStore
) : ViewModel() {

    var state by mutableStateOf<HomeChannelsPanelState>(HomeChannelsPanelState.Unselected)
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

    fun selectChannel(channelId: Long) {
        viewModelScope.launch {
            persistentDataStore.updateCurrentChannel(channelId)
        }
    }

    fun toggleCategory(categoryId: Long) {
        viewModelScope.launch {
            persistentDataStore.toggleCategory(categoryId)
        }
        categoryChannels[categoryId]?.apply { collapsed = !collapsed }
    }

    init {
        persistentDataStore.observeCurrentGuild()
            .collectIn(viewModelScope) { guildId ->
                if (guildId == 0L) {
                    state = HomeChannelsPanelState.Unselected
                    return@collectIn
                }

                state = HomeChannelsPanelState.Loading

                val guild = guildStore.fetchGuild(guildId)
                if (guild == null) {
                    state = HomeChannelsPanelState.Error
                    return@collectIn
                }

                guildName = guild.name
                guildBannerUrl = guild.bannerUrl
                guildBoostLevel = guild.premiumTier

                replaceChannels(channelStore.fetchChannels(guildId))
            }

        persistentDataStore.observeCurrentGuild()
            .mapNotNull { guildId ->
                if (guildId == 0L) null else guildStore.observeGuild(guildId)
            }.flattenMerge().collectIn(viewModelScope) { event ->
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
                        state = HomeChannelsPanelState.Unselected
                    },
                )
            }

        persistentDataStore.observeCurrentGuild()
            .mapNotNull { guildId ->
                if (guildId == 0L) null else channelStore.observeChannelsReplace(guildId)
            }.flattenMerge().collectIn(viewModelScope) { channels ->
                replaceChannels(channels)
            }

        persistentDataStore.observeCurrentGuild()
            .mapNotNull { guildId ->
                if (guildId == 0L) null else channelStore.observeChannels(guildId)
            }.flattenMerge().collectIn(viewModelScope) { event ->
            state = HomeChannelsPanelState.Loaded
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

        persistentDataStore.observeCollapsedCategories()
            .collectIn(viewModelScope) { collapsedCategories ->
                collapsedCategories.forEach {
                    categoryChannels[it]
                }
            }
    }


    private suspend fun makeAliveChannelItem(channel: DomainChannel): ChannelItemData {
        if (channel is DomainCategoryChannel) {
            error("cannot make channel item from category channel")
        }

        val unreadState = unreadStore.getChannel(channel.id)
        val item = ChannelItemData(
            channel = channel,
            mentionCount = unreadState?.mentionCount ?: 0,
            lastUnreadMessageId = unreadState?.lastMessageId,
            lastChannelMessageId = lastMessageStore.getLastMessageId(channel.id),
        )

        item.unreadListenerJob = unreadStore.observeUnreadState(channel.id).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = item::updateUnreadState,
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
                onAdd = { (_, messageId) -> item.updateLastMessageId(messageId) },
                onUpdate = { },
                onDelete = { /* Handled by UnreadStore listener */ },
            )
        }

        item.mentionCountListenerJob = unreadStore.observeMentionCount(channel.id).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = { item.mentionCount = it },
                onUpdate = { item.mentionCount += it },
                onDelete = {},
            )
        }

        return item
    }

    private suspend fun replaceChannels(channels: List<DomainChannel>) {
        val collapsedCategories = persistentDataStore.observeCollapsedCategories().last()
        val (categoryItems, channelItems) = channels
            .partition { it is DomainCategoryChannel }
            .let { (newCategories, newChannels) ->
                val channelItems = newChannels.associate {
                    it.id to makeAliveChannelItem(it)
                }

                val categoryItems = newCategories.associate { category ->
                    category.id to CategoryItemData(
                        channel = category as DomainCategoryChannel,
                        collapsed = collapsedCategories.contains(category.id),
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
            synchronized(state) { // Needed or the old cached items can overwrite the new items from gw
                allChannelItems.values.forEach { it.cancelJobs() }
                allChannelItems.clear()
                allChannelItems.putAll(channelItems)

                categoryChannels.clear()
                categoryChannels.putAll(categoryItems)

                noCategoryChannels.clear()
                noCategoryChannels.putAll(noCategoryItems)
                state = HomeChannelsPanelState.Loaded
            }
        }
    }

}
