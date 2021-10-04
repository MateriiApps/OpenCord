package com.xinto.opencord.ui.screens.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.People
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.xinto.opencord.ui.component.appbar.AppBar
import com.xinto.opencord.ui.component.layout.OpenCordBackground
import com.xinto.opencord.ui.component.text.Text
import com.xinto.opencord.ui.theme.topLargeCorners
import com.xinto.opencord.ui.viewmodel.MainViewModel
import com.xinto.opencord.ui.widgets.chat.WidgetChatMessage
import com.xinto.opencord.ui.widgets.textfield.ChannelTextField

@Composable
fun CenterPanel(
    viewModel: MainViewModel,
    onChannelsButtonClick: () -> Unit,
    onMembersButtonClick: () -> Unit
) {
    val currentChannel by viewModel.currentChannel.collectAsState()

    val messageData = viewModel.messages[currentChannel?.channelId]

    var message by rememberSaveable(currentChannel) { mutableStateOf("") }

    OpenCordBackground(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.topLargeCorners)
    ) {
        Crossfade(targetState = messageData) { messages ->
            Column {
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
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    reverseLayout = true
                ) {
                    if (messages != null) {
                        items(messages.messages) { message ->
                            WidgetChatMessage(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                                message = message
                            )
                        }
                    } else {
                        item {
                            CircularProgressIndicator()
                        }
                    }
                }
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
        }
    }
}