package com.xinto.opencord.ui.screens.home.panels.messagemenu

import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import com.xinto.opencord.ui.screens.home.panels.messagemenu.state.MessageMenuLoaded
import com.xinto.opencord.ui.util.getLocalViewModel
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

@Composable
fun MessageMenu(
    messageId: Long,
    onDismiss: (() -> Unit)? = null,
    sheetState: SheetState = rememberModalBottomSheetState(),
    viewModel: MessageMenuViewModel =
        getLocalViewModel(parameters = { parametersOf(messageId) }),
) {
    val coroutineScope = rememberCoroutineScope()
    var firstRender by remember { mutableStateOf(true) }

    LaunchedEffect(sheetState.isVisible) {
        if (firstRender) {
            firstRender = false
        } else if (!sheetState.isVisible) {
            onDismiss?.invoke()
        }
    }

    LaunchedEffect(viewModel.state) {
        if (viewModel.state == HomeMessageMenuPanelState.Closing) {
            onDismiss?.invoke()
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            coroutineScope.launch {
                onDismiss?.invoke()
            }
        },
    ) {
        when (viewModel.state) {
            HomeMessageMenuPanelState.Loading -> MessageMenuLoading()
            HomeMessageMenuPanelState.Loaded -> MessageMenuLoaded(
                viewModel = viewModel,
            )
            HomeMessageMenuPanelState.Closing -> {
                LaunchedEffect(Unit) {
                    sheetState.hide()
                }
            }
        }
    }
}
