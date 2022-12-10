package com.xinto.opencord.ui.screens.home.panels.guild

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.xinto.opencord.ui.viewmodel.GuildsViewModel
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.getViewModel

@Composable
fun GuildsList(
    onGuildSelect: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuildsViewModel = getViewModel()
) {
    val guilds by remember(viewModel.guilds) {
        derivedStateOf {
            viewModel.guilds.values.toImmutableList()
        }
    }

    when (viewModel.state) {
        GuildsViewModel.State.Loading -> {
            GuildsListLoading(modifier = modifier)
        }
        GuildsViewModel.State.Loaded -> {
            GuildsListLoaded(
                modifier = modifier,
                onGuildSelect = {
                    viewModel.selectGuild(it)
                    onGuildSelect()
                },
                guilds = guilds,
                selectedGuildId = viewModel.selectedGuildId,
            )
        }
        GuildsViewModel.State.Error -> {

        }
    }
}
