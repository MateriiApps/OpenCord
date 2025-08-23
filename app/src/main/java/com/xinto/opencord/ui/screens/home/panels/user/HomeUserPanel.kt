package com.xinto.opencord.ui.screens.home.panels.user

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.domain.usersettings.DomainCustomStatus
import com.xinto.opencord.domain.usersettings.DomainUserStatus
import com.xinto.opencord.ui.screens.home.panels.user.component.CurrentUserSheet
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeUserPanel(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit
) {
    val viewModel: HomeUserPanelViewModel = koinViewModel()
    HomeUserPanel(
        modifier = modifier,
        onSettingsClick = onSettingsClick,
        state = viewModel.state,
        avatarUrl = viewModel.avatarUrl,
        username = viewModel.username,
        discriminator = viewModel.discriminator,
        status = viewModel.userStatus,
        isStreaming = viewModel.isStreaming,
        customStatus = viewModel.userCustomStatus,
    )
}

@Composable
fun HomeUserPanel(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
    state: HomeUserPanelState,
    avatarUrl: String,
    username: String,
    discriminator: String,
    status: DomainUserStatus?,
    isStreaming: Boolean,
    customStatus: DomainCustomStatus?
) {
    var showStatusSheet by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        onClick = { showStatusSheet = true },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp,
    ) {
        when (state) {
            HomeUserPanelState.Loading -> {
                CurrentUserLoading(
                    onSettingsClick = onSettingsClick,
                )
            }
            HomeUserPanelState.Loaded -> {
                CurrentUserLoaded(
                    onSettingsClick = onSettingsClick,
                    avatarUrl = avatarUrl,
                    username = username,
                    discriminator = discriminator,
                    status = status,
                    isStreaming = isStreaming,
                    customStatus = customStatus,
                )
            }
            HomeUserPanelState.Error -> {
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
