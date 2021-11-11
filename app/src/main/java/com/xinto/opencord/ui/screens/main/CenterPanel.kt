package com.xinto.opencord.ui.screens.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.People
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.component.appbar.AppBar
import com.xinto.opencord.ui.component.bottomsheet.BottomSheetDialog
import com.xinto.opencord.ui.component.button.FullWidthButton
import com.xinto.opencord.ui.component.layout.OpenCordBackground
import com.xinto.opencord.ui.component.text.Text
import com.xinto.opencord.ui.theme.topLargeCorners
import com.xinto.opencord.ui.viewmodel.MainViewModel
import com.xinto.opencord.ui.widgets.chat.WidgetChatMessage
import com.xinto.opencord.ui.widgets.textfield.ChannelTextField
import com.xinto.opencord.util.SimpleAstParser

@ExperimentalFoundationApi
@Composable
fun CenterPanel(
    viewModel: MainViewModel,
    parser: SimpleAstParser,
    onChannelsButtonClick: () -> Unit,
    onMembersButtonClick: () -> Unit
) {
    val currentChannel by viewModel.currentChannel.collectAsState()

    val messageData = viewModel.messages[currentChannel?.channelId]

    var message by rememberSaveable(currentChannel, key = "message-${currentChannel?.channelId}") { mutableStateOf("") }

    OpenCordBackground(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.topLargeCorners)
    ) {
        Crossfade(targetState = messageData) { messages ->
            Scaffold(
                topBar = {
                    AppBar(
                        title = {
                            Text(text = currentChannel?.channelName.toString())
                        },
                        navigation = {
                            IconButton(onChannelsButtonClick) {
                                Icon(
                                    imageVector = Icons.Rounded.Menu,
                                    contentDescription = null
                                )
                            }
                        },
                        actions = {
                            IconButton(onMembersButtonClick) {
                                Icon(
                                    imageVector = Icons.Rounded.People,
                                    contentDescription = null
                                )
                            }
                        },
                    )
                },
                bottomBar = {
                    ChannelTextField(
                        value = message,
                        onValueChange = {
                            message = it
                        },
                        onSendClick = {
                            viewModel.sendMessage(message)
                            message = ""
                        }
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    reverseLayout = true,
                ) {
                    if (messages != null) {
                        items(messages.messages) { message ->
                            var showBottomDialog by rememberSaveable { mutableStateOf(false) }

                            WidgetChatMessage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.medium)
                                    .combinedClickable(
                                        onLongClick = {
                                            showBottomDialog = true
                                        },
                                        onClick = {}
                                    )
                                    .padding(6.dp),
                                message = message,
                                parser = parser,
                            )

                            if (showBottomDialog) {
                                BottomSheetDialog(
                                    onDismissRequest = {
                                        showBottomDialog = false
                                    }
                                ) {
                                    Surface(
                                        color = MaterialTheme.colors.background
                                    ) {
                                        Column {
                                            FullWidthButton(
                                                modifier = Modifier.fillMaxWidth(),
                                                icon = {},
                                                content = {
                                                    Text("Reply")
                                                }
                                            ) {

                                            }
                                            FullWidthButton(
                                                modifier = Modifier.fillMaxWidth(),
                                                icon = {},
                                                content = {
                                                    Text("Create Thread")
                                                }
                                            ) {

                                            }
                                            FullWidthButton(
                                                modifier = Modifier.fillMaxWidth(),
                                                icon = {},
                                                content = {
                                                    Text("Copy Text")
                                                }
                                            ) {

                                            }
                                            FullWidthButton(
                                                modifier = Modifier.fillMaxWidth(),
                                                icon = {},
                                                content = {
                                                    Text("Delete")
                                                }
                                            ) {

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}