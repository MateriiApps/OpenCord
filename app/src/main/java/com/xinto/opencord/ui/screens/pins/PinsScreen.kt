package com.xinto.opencord.ui.screens.pins

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.ui.navigation.PinsScreenData
import com.xinto.opencord.ui.util.ContentAlpha
import com.xinto.opencord.ui.util.VoidablePaddingValues
import com.xinto.opencord.ui.util.paddingOrGestureNav
import com.xinto.opencord.ui.viewmodel.ChannelPinsViewModel
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PinsScreen(
    data: PinsScreenData,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChannelPinsViewModel = getViewModel { parametersOf(data) }
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val pins by remember(viewModel.messages) {
        derivedStateOf {
            viewModel.messages.values
                .sortedByDescending { it.timestamp }
                .toImmutableList()
        }
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(stringResource(R.string.pins_title))

                        if (viewModel.channelName != null) {
                            Text(
                                text = "#${viewModel.channelName}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .alpha(ContentAlpha.medium)
                                    .offset(y = (-2).dp)
                                    .padding(bottom = 1.dp),
                            )
                        }
                    }
                },
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
                        .paddingOrGestureNav(paddingValues),
                )
            }
            is ChannelPinsViewModel.State.Loaded -> {
                PinsScreenLoaded(
                    pins = pins,
                    contentPadding = VoidablePaddingValues(paddingValues, top = false),
                    modifier = Modifier
                        .fillMaxSize()
                        .paddingOrGestureNav(paddingValues),
                )
            }
            is ChannelPinsViewModel.State.Error -> {
                PinsScreenError(
                    modifier = Modifier
                        .fillMaxSize()
                        .paddingOrGestureNav(paddingValues),
                )
            }
        }
    }
}
