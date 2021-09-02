package com.xinto.opencord.ui.screens.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.xinto.opencord.ui.component.overlappingpanels.OpenCordOverlappingPanels
import com.xinto.opencord.ui.component.overlappingpanels.OverlappingPanelValue
import com.xinto.opencord.ui.component.overlappingpanels.rememberOverlappingPanelState
import com.xinto.opencord.ui.viewmodel.MainViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen() {
    val panelState = rememberOverlappingPanelState(initialValue = OverlappingPanelValue.Closed)

    val viewModel: MainViewModel = getViewModel()

    OpenCordOverlappingPanels(
        modifier = Modifier.fillMaxSize(),
        panelState = panelState,
        panelLeft = {
            LeftPanel()
        },
        panelMiddle = {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = Color.Cyan,
                    size = size
                )
            }
        },
        panelRight = {
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