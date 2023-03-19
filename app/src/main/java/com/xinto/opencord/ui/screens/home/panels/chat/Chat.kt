package com.xinto.opencord.ui.screens.home.panels.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.ui.screens.home.panels.chatinput.ChatInput
import com.xinto.opencord.ui.viewmodel.ChatViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun Chat(
    onChannelsButtonClick: () -> Unit,
    onMembersButtonClick: () -> Unit,
    onPinsButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = getViewModel(),
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (viewModel.channelName.isNotEmpty()) {
                            Icon(
                                painter = painterResource(R.drawable.ic_tag),
                                contentDescription = null,
                                modifier = Modifier,
                            )
                        }
                        Text(
                            text = viewModel.channelName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                },
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
                            painter = painterResource(R.drawable.ic_pin),
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
            modifier = Modifier.fillMaxSize(),
            tonalElevation = 2.dp,
        ) {
            when (viewModel.state) {
                is ChatViewModel.State.Unselected -> {
                    ChatUnselected(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    )
                }
                is ChatViewModel.State.Loading -> {
                    ChatLoading(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    )
                }
                is ChatViewModel.State.Loaded -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    ) {
                        ChatLoaded(
                            viewModel = viewModel,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                        )

                        ChatInput(
                            modifier = Modifier.padding(
                                start = 8.dp,
                                end = 8.dp,
                                bottom = 4.dp,
                            ),
                            hint = {
                                Text(stringResource(R.string.chat_input_hint, viewModel.channelName))
                            },
                        )
                    }
                }
                is ChatViewModel.State.Error -> {
                    ChatError(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    )
                }
            }
        }
    }
}
