package com.xinto.opencord.ui.screens.home.panels.channel

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.viewmodel.ChannelsViewModel
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentHashMap

@Composable
fun ChannelsList(
    onChannelSelect: () -> Unit,
    viewModel: ChannelsViewModel,
    modifier: Modifier = Modifier
) {
    val sortedChannels by remember(viewModel.channels) {
        derivedStateOf {
            viewModel.getSortedChannels()
                .mapValues { it.value.toImmutableList() }
                .toPersistentHashMap()
        }
    }
    val collapsedCategories by remember(viewModel.collapsedCategories) {
        derivedStateOf {
            viewModel.collapsedCategories.toImmutableList()
        }
    }
    val unreadChannels by remember(viewModel.unreadStates, viewModel.lastMessageIds, viewModel.channels) {
        derivedStateOf {
            persistentHashMapOf<Long, Boolean>().builder().apply {
                for (channel in viewModel.channels.values) {
                    put(channel.id, (viewModel.lastMessageIds[channel.id] ?: 0) > (viewModel.unreadStates[channel.id]?.lastMessageId ?: -1))
                }
            }.build()
        }
    }

    CompositionLocalProvider(LocalAbsoluteTonalElevation provides 1.dp) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.large,
        ) {
            when (viewModel.state) {
                is ChannelsViewModel.State.Unselected -> {
                    ChannelsListUnselected(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is ChannelsViewModel.State.Loading -> {
                    ChannelsListLoading(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is ChannelsViewModel.State.Loaded -> {
                    ChannelsListLoaded(
                        modifier = Modifier.fillMaxSize(),
                        onChannelSelect = {
                            viewModel.selectChannel(it)
                            onChannelSelect()
                        },
                        onCategoryClick = {
                            viewModel.toggleCategory(it)
                        },
                        bannerUrl = viewModel.guildBannerUrl,
                        boostLevel = viewModel.guildBoostLevel,
                        guildName = viewModel.guildName,
                        channels = sortedChannels,
                        unreadChannels = unreadChannels,
                        collapsedCategories = collapsedCategories,
                        selectedChannelId = viewModel.selectedChannelId,
                    )
                }
                is ChannelsViewModel.State.Error -> {

                }
            }
        }
    }
}
