package com.xinto.opencord.ui.screens.home.panels.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.emoji.DomainEmoji
import com.xinto.opencord.ui.navigation.PinsScreenData
import com.xinto.opencord.ui.screens.home.panels.chat.component.HomeChatPanelInput
import com.xinto.opencord.ui.screens.home.panels.chat.component.HomeChatPanelTopBar
import com.xinto.opencord.ui.screens.home.panels.chat.model.MessageItem
import com.xinto.opencord.ui.screens.home.panels.chat.state.ChatError
import com.xinto.opencord.ui.screens.home.panels.chat.state.ChatLoaded
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeChatPanel(
    modifier: Modifier = Modifier,
    onPinsButtonClick: (PinsScreenData) -> Unit,
    onMembersButtonClick: () -> Unit,
    onChannelsButtonClick: () -> Unit,
) {
    val viewModel: HomeChatPanelViewModel = koinViewModel()
    HomeChatPanel(
        modifier = modifier,
        state = viewModel.state,
        channelName = viewModel.channelName,
        messages = viewModel.sortedMessages,
        onMessageReact = viewModel::reactToMessage,
        onPinsButtonClick = {
            onPinsButtonClick(PinsScreenData(viewModel.channelId))
        },
        onMembersButtonClick = onMembersButtonClick,
        onChannelsButtonClick = onChannelsButtonClick,
        inputText = viewModel.inputText,
        onInputTextChange = viewModel::updateInputText,
        onInputTextSend = viewModel::sendMessage
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
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onInputTextSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            HomeChatPanelTopBar(
                channelName = channelName,
                onChannelsButtonClick = onChannelsButtonClick,
                onPinsButtonClick = onPinsButtonClick,
                onMembersButtonClick = onMembersButtonClick
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
                            text = inputText,
                            onTextChange = onInputTextChange,
                            onSend = onInputTextSend,
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
