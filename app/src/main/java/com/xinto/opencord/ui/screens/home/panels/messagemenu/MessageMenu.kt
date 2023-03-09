package com.xinto.opencord.ui.screens.home.panels.messagemenu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.xinto.opencord.ui.util.getLocalViewModel
import com.xinto.opencord.ui.viewmodel.MessageMenuViewModel
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

@Composable
fun MessageMenu(
    messageId: Long,
    onDismiss: (() -> Unit)? = null,
    sheetState: SheetState = remember {
        SheetState(
            skipCollapsed = false,
            initialValue = SheetValue.Hidden,
            confirmValueChange = { true },
        )
    },
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

    BackHandler {
        coroutineScope.launch {
            sheetState.hide()
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
            MessageMenuViewModel.State.Loading -> MessageMenuLoading()
            MessageMenuViewModel.State.Loaded -> MessageMenuLoaded(
                viewModel = viewModel,
            )
            MessageMenuViewModel.State.Closing -> {
                LaunchedEffect(Unit) {
                    sheetState.hide()
                }
            }
        }

        Spacer(modifier = Modifier.fillMaxHeight())
    }
}
