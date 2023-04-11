package com.xinto.opencord.ui.screens.home.panels.guild

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xinto.opencord.ui.viewmodel.GuildsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun GuildsList(
    onGuildSelect: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuildsViewModel = getViewModel(),
) {
    when (viewModel.state) {
        GuildsViewModel.State.Loading -> {
            GuildsListLoading(modifier = modifier)
        }
        GuildsViewModel.State.Loaded -> {
            GuildsListLoaded(
                items = viewModel.listItems,
                modifier = modifier,
                onGuildSelect = {
                    viewModel.selectGuild(it)
                    onGuildSelect()
                },
            )
        }
        GuildsViewModel.State.Error -> {

        }
    }
}
