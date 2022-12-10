package com.xinto.opencord.ui.screens.home.panels.currentuser

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.screens.home.panels.currentuser.sheet.CurrentUserSheet
import com.xinto.opencord.ui.viewmodel.CurrentUserViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun CurrentUser(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
    viewModel: CurrentUserViewModel = getViewModel()
) {
    var showStatusSheet by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        onClick = { showStatusSheet = true },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp,
    ) {
        when (viewModel.state) {
            CurrentUserViewModel.State.Loading -> {
                CurrentUserLoading(
                    onSettingsClick = onSettingsClick,
                )
            }
            CurrentUserViewModel.State.Loaded -> {
                CurrentUserLoaded(
                    onSettingsClick = onSettingsClick,
                    avatarUrl = viewModel.avatarUrl,
                    username = viewModel.username,
                    discriminator = viewModel.discriminator,
                    status = viewModel.userStatus,
                    isStreaming = viewModel.isStreaming,
                    customStatus = viewModel.userCustomStatus,
                )
            }
            CurrentUserViewModel.State.Error -> {
                // TODO: CurrentUserError
            }
        }
    }

    if (showStatusSheet) {
        CurrentUserSheet(
            onClose = { showStatusSheet = false },
        )
    }
}
