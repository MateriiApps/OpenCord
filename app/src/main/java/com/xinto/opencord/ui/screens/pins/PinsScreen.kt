package com.xinto.opencord.ui.screens.pins

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.xinto.opencord.R
import com.xinto.opencord.ui.viewmodel.ChannelPinsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun PinsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChannelPinsViewModel = getViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = { Text(stringResource(R.string.pins_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        when (viewModel.state) {
            is ChannelPinsViewModel.State.Loading -> {
                PinsScreenLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }
            is ChannelPinsViewModel.State.Loaded -> {
                PinsScreenLoaded(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    pins = viewModel.pins.values.sortedByDescending {
                        it.timestamp
                    },
                )
            }
            is ChannelPinsViewModel.State.Error -> {
                PinsScreenError(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                )
            }
        }
    }
}
