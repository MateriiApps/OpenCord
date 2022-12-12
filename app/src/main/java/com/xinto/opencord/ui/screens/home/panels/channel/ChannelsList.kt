package com.xinto.opencord.ui.screens.home.panels.channel

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.viewmodel.ChannelsViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ChannelsList(
    guildId: Long,
    onChannelSelect: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ChannelsViewModel = koinViewModel { parametersOf(guildId) }
    val sortedChannels by remember(viewModel.channels) {
        derivedStateOf {
            viewModel.getSortedChannels()
                .mapValues { it.value.toImmutableList() }
                .toImmutableMap()
        }
    }
    val collapsedCategories by remember(viewModel.collapsedCategories) {
        derivedStateOf {
            viewModel.collapsedCategories.toImmutableList()
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
                            onChannelSelect(it)
                        },
                        onCategoryClick = {
                            viewModel.toggleCategory(it)
                        },
                        bannerUrl = viewModel.guildBannerUrl,
                        boostLevel = viewModel.guildBoostLevel,
                        guildName = viewModel.guildName,
                        channels = sortedChannels,
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
