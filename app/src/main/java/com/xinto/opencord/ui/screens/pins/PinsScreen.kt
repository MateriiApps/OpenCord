package com.xinto.opencord.ui.screens.pins

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.ui.navigation.AppNavigator
import com.xinto.opencord.ui.navigation.PinsScreenData
import com.xinto.opencord.ui.screens.pins.component.PinsTopBar
import com.xinto.opencord.ui.screens.pins.state.PinsScreenError
import com.xinto.opencord.ui.screens.pins.state.PinsScreenLoaded
import com.xinto.opencord.ui.screens.pins.state.PinsScreenLoading
import com.xinto.opencord.ui.util.VoidablePaddingValues
import com.xinto.opencord.ui.util.paddingOrGestureNav
import com.xinto.opencord.ui.util.toUnsafeImmutableList
import dev.olshevski.navigation.reimagined.pop
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PinsScreen(
    data: PinsScreenData,
    modifier: Modifier = Modifier,
    navigator: AppNavigator
) {
    val viewModel: PinsScreenViewModel = koinViewModel { parametersOf(data) }
    PinsScreen(
        onBackClick =  {
            navigator.pop()
        },
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
            PinsTopBar(
                channelName = channelName,
                onBackClick = onBackClick,
                scrollBehavior = scrollBehavior
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
