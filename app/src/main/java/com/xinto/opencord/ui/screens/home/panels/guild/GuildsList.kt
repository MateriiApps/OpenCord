package com.xinto.opencord.ui.screens.home.panels.guild

import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.xinto.opencord.ui.screens.home.panels.guild.model.GuildItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeGuildsPanel(modifier: Modifier = Modifier) {
    val viewModel: GuildsViewModel = koinViewModel()
    HomeGuildsPanel(
        modifier = modifier,
        onGuildSelect = viewModel::selectGuild,
        state = viewModel.state,
        guilds = viewModel.listItems
    )
}

@Composable
fun HomeGuildsPanel(
    onGuildSelect: (Long) -> Unit,
    state: HomeGuildPanelState,
    guilds: SnapshotStateList<GuildItem>,
    modifier: Modifier = Modifier,
) {
    when (state) {
        HomeGuildPanelState.Loading -> {
            GuildsListLoading(modifier = modifier)
        }
        HomeGuildPanelState.Loaded -> {
            GuildsListLoaded(
                items = guilds,
                modifier = modifier,
                onGuildSelect = onGuildSelect,
            )
        }
        HomeGuildPanelState.Error -> {

        }
    }
}
