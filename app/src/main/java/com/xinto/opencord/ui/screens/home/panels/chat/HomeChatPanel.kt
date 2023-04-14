package com.xinto.opencord.ui.screens.home.panels.chat

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.emoji.DomainEmoji
import com.xinto.opencord.ui.screens.home.panels.chat.state.ChatError
import com.xinto.opencord.ui.screens.home.panels.chat.state.ChatLoaded
import com.xinto.opencord.ui.screens.home.panels.chat.component.HomeChatPanelInput
import com.xinto.opencord.ui.screens.home.panels.chat.model.MessageItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeChatPanel(modifier: Modifier = Modifier) {
    val viewModel: HomeChatPanelViewModel = koinViewModel()
    HomeChatPanel(
        modifier = modifier,
        state = viewModel.state,
        channelName = viewModel.channelName,
        messages = viewModel.sortedMessages,
        onMessageReact = viewModel::reactToMessage,
        onPinsButtonClick = { /*TODO*/ },
        onMembersButtonClick = { /*TODO*/ },
        onChannelsButtonClick = { /*TODO*/ }
    )
}

@Composable
fun HomeChatPanel(
    state: HomeChatPanelState,
    channelName: String,
    messages: SnapshotStateList<MessageItem>,
    onMessageReact: (Long, DomainEmoji) -> Unit,
    onPinsButtonClick: () -> Unit,
    onMembersButtonClick: () -> Unit,
    onChannelsButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
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
                        if (channelName.isNotEmpty()) {
                            Icon(
                                painter = painterResource(R.drawable.ic_tag),
                                contentDescription = null,
                                modifier = Modifier,
                            )
                        }
                        Text(
                            text = channelName,
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
            when (state) {
                is HomeChatPanelState.Unselected -> {
                    ChatUnselected(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    )
                }
                is HomeChatPanelState.Loading -> {
                    ChatLoading(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    )
                }
                is HomeChatPanelState.Loaded -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    ) {
                        ChatLoaded(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            messages = messages,
                            onMessageReact = onMessageReact
                        )

                        HomeChatPanelInput(
                            modifier = Modifier.padding(
                                start = 8.dp,
                                end = 8.dp,
                                bottom = 4.dp,
                            ),
                            hint = {
                                Text(stringResource(R.string.chat_input_hint, channelName))
                            },
                        )
                    }
                }
                is HomeChatPanelState.Error -> {
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
