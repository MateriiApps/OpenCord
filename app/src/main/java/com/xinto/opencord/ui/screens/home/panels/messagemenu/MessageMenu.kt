package com.xinto.opencord.ui.screens.home.panels.messagemenu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.xinto.opencord.ui.util.getLocalViewModel
import com.xinto.opencord.ui.viewmodel.MessageMenuViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MessageMenu(
    messageId: Long,
    onDismiss: (() -> Unit)? = null,
    sheetState: SheetState = remember {
        SheetState(false, confirmValueChange = { true })
    },
    viewModel: MessageMenuViewModel =
        getLocalViewModel(parameters = { parametersOf(messageId) }),
) {
    BackHandler {
        onDismiss?.invoke()
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { onDismiss?.invoke() },
    ) {
        when (viewModel.state) {
            MessageMenuViewModel.State.Loading -> MessageMenuLoading()
            MessageMenuViewModel.State.Loaded -> MessageMenuLoaded(
                viewModel = viewModel,
            )
            MessageMenuViewModel.State.Closing -> {
                onDismiss?.invoke()
            }
        }

        Spacer(modifier = Modifier.fillMaxHeight())
    }
}
