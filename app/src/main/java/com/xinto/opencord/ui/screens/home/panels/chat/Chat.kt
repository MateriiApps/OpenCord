package com.xinto.opencord.ui.screens.home.panels.chat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.ui.viewmodel.ChatViewModel
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.getViewModel

@Composable
fun Chat(
    onChannelsButtonClick: () -> Unit,
    onMembersButtonClick: () -> Unit,
    onPinsButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = getViewModel(),
) {
    val sortedMessages by remember(viewModel.messages) {
        derivedStateOf { viewModel.getSortedMessages().toImmutableList() }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.chat_title, viewModel.channelName)) },
                navigationIcon = {
                    IconButton(onChannelsButtonClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_menu),
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onPinsButtonClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_push_pin),
                            contentDescription = null,
                        )
                    }
                    IconButton(onMembersButtonClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_people),
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            tonalElevation = 2.dp,
        ) {
            when (viewModel.state) {
                is ChatViewModel.State.Unselected -> {
                    ChatUnselected(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is ChatViewModel.State.Loading -> {
                    ChatLoading(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is ChatViewModel.State.Loaded -> {
                    ChatLoaded(
                        messages = sortedMessages,
                        reactions = viewModel.reactions,
                        currentUserId = viewModel.currentUserId,
                        channelName = viewModel.channelName,
                        userMessage = viewModel.userMessage,
                        sendEnabled = viewModel.sendEnabled,
                        onUserMessageUpdate = viewModel::updateMessage,
                        onUserMessageSend = viewModel::sendMessage,
                        onMessageReact = viewModel::reactToMessage,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is ChatViewModel.State.Error -> {
                    ChatError(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
