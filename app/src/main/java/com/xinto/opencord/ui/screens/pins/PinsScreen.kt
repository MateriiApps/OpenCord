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
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.ui.navigation.PinsScreenData
import com.xinto.opencord.ui.screens.pins.state.PinsScreenError
import com.xinto.opencord.ui.screens.pins.state.PinsScreenLoaded
import com.xinto.opencord.ui.screens.pins.state.PinsScreenLoading
import com.xinto.opencord.ui.util.ContentAlpha
import com.xinto.opencord.ui.util.VoidablePaddingValues
import com.xinto.opencord.ui.util.paddingOrGestureNav
import com.xinto.opencord.ui.util.toUnsafeImmutableList
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PinsScreen(
    data: PinsScreenData,
    modifier: Modifier,
    onBackClick: () -> Unit,
) {
    val viewModel: PinsScreenViewModel = koinViewModel { parametersOf(data) }
    PinsScreen(
        onBackClick = onBackClick,
        state = viewModel.state,
        modifier = modifier,
        messages = viewModel.messages,
        channelName = viewModel.channelName
    )
}

@Composable
fun PinsScreen(
    state: PinsScreenState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    messages: SnapshotStateMap<Long, DomainMessage>,
    channelName: String?,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val pins by remember(messages) {
        derivedStateOf {
            messages.values
                .sortedByDescending { it.timestamp }
                .toUnsafeImmutableList()
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

                        if (channelName != null) {
                            Text(
                                text = "#${channelName}",
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
        when (state) {
            is PinsScreenState.Loading -> {
                PinsScreenLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .paddingOrGestureNav(paddingValues),
                )
            }
            is PinsScreenState.Loaded -> {
                PinsScreenLoaded(
                    pins = pins,
                    contentPadding = VoidablePaddingValues(paddingValues, top = false),
                    modifier = Modifier
                        .fillMaxSize()
                        .paddingOrGestureNav(paddingValues),
                )
            }
            is PinsScreenState.Error -> {
                PinsScreenError(
                    modifier = Modifier
                        .fillMaxSize()
                        .paddingOrGestureNav(paddingValues),
                )
            }
        }
    }
}
