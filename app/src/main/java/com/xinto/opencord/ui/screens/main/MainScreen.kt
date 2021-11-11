package com.xinto.opencord.ui.screens.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets
import com.xinto.opencord.ui.component.layout.OpenCordBackground
import com.xinto.opencord.ui.viewmodel.MainViewModel
import com.xinto.opencord.util.SimpleAstParser
import com.xinto.overlappingpanels.OverlappingPanels
import com.xinto.overlappingpanels.OverlappingPanelsValue
import com.xinto.overlappingpanels.rememberOverlappingPanelsState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@ExperimentalFoundationApi
@ExperimentalMaterialApi
@Composable
fun MainScreen() {
    val panelState = rememberOverlappingPanelsState(initialValue = OverlappingPanelsValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val viewModel: MainViewModel = getViewModel()
    val simpleAstParser: SimpleAstParser = get()

    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
        OpenCordBackground(
            backgroundColorAlpha = 0.2f
        ) {
            OverlappingPanels(
                modifier = Modifier.fillMaxSize(),
                panelsState = panelState,
                panelStart = {
                    StartPanel(viewModel, panelState)
                },
                panelCenter = {
                    CenterPanel(
                        viewModel = viewModel,
                        parser = simpleAstParser,
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
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        drawRect(
                            color = Color.Magenta,
                            size = size
                        )
                    }
                }
            )
        }
    }
}