package com.xinto.opencord.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.xinto.opencord.ui.viewmodel.ChannelsViewModel
import com.xinto.opencord.ui.viewmodel.ChatViewModel
import com.xinto.opencord.ui.viewmodel.GuildsViewModel
import com.xinto.overlappingpanels.OverlappingPanels
import com.xinto.overlappingpanels.OverlappingPanelsValue
import com.xinto.overlappingpanels.rememberOverlappingPanelsState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val panelState = rememberOverlappingPanelsState(OverlappingPanelsValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val chatViewModel: ChatViewModel = getViewModel()
    val guildsViewModel: GuildsViewModel = getViewModel()
    val channelsViewModel: ChannelsViewModel = getViewModel()

    Surface(modifier = Modifier.fillMaxSize()) {
        OverlappingPanels(
            modifier = Modifier.fillMaxSize(),
            panelsState = panelState,
            panelStart = {
                GuildsChannelsScreen(
                    onGuildSelect = {
                        channelsViewModel.load(it)
                    },
                    onChannelSelect = {
                        chatViewModel.load(it)
                    },
                    guildsViewModel = guildsViewModel,
                    channelsViewModel = channelsViewModel
                )
            },
            panelCenter = {
                ChatScreen(
                    viewModel = chatViewModel,
                    onChannelsButtonClick = {
                        coroutineScope.launch {
                            panelState.openStartPanel()
                        }
                    },
                    onMembersButtonClick = {
                        coroutineScope.launch {
                            panelState.openEndPanel()
                        }
                    },
                )
            },
            panelEnd = {
                ChannelMembersScreen()
            }
        )
    }
}