package com.xinto.opencord.ui.screens.home.panels.channel

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.screens.home.panels.channel.model.CategoryItemData
import com.xinto.opencord.ui.screens.home.panels.channel.model.ChannelItemData
import com.xinto.opencord.ui.screens.home.panels.channel.state.ChannelsListLoaded
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeChannelsPanel(modifier: Modifier = Modifier) {
    val viewModel: HomeChannelsPanelViewModel = koinViewModel()

    HomeChannelsPanel(
        state = viewModel.state,
        onChannelSelect = viewModel::selectChannel,
        onCategoryClick = viewModel::toggleCategory,
        guildBannerUrl = viewModel.guildBannerUrl,
        guildBoostLevel = viewModel.guildBoostLevel,
        guildName = viewModel.guildName,
        selectedChannelId = viewModel.selectedChannelId,
        categoryChannels = viewModel.categoryChannels,
        noCategoryChannels = viewModel.noCategoryChannels,
        modifier = modifier,
    )
}

@Composable
fun HomeChannelsPanel(
    state: HomeChannelsPanelState,
    onChannelSelect: (Long) -> Unit,
    onCategoryClick: (Long) -> Unit,
    guildBannerUrl: String?,
    guildBoostLevel: Int,
    guildName: String,
    selectedChannelId: Long,
    categoryChannels: SnapshotStateMap<Long, CategoryItemData>,
    noCategoryChannels: SnapshotStateMap<Long, ChannelItemData>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    CompositionLocalProvider(LocalAbsoluteTonalElevation provides 1.dp) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.large,
        ) {
            when (state) {
                is HomeChannelsPanelState.Unselected -> {
                    ChannelsListUnselected(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is HomeChannelsPanelState.Loading -> {
                    ChannelsListLoading(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is HomeChannelsPanelState.Loaded -> {
                    ChannelsListLoaded(
                        modifier = Modifier.fillMaxSize(),
                        onChannelSelect = onChannelSelect,
                        onCategoryClick = onCategoryClick,
                        bannerUrl = guildBannerUrl,
                        boostLevel = guildBoostLevel,
                        guildName = guildName,
                        selectedChannelId = selectedChannelId,
                        categoryChannels = categoryChannels,
                        noCategoryChannels = noCategoryChannels,
                        lazyListState = lazyListState,
                    )
                }
                is HomeChannelsPanelState.Error -> {

                }
            }
        }
    }
}
