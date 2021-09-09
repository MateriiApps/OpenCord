package com.xinto.opencord.ui.screens.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.network.result.DiscordAPIResult
import com.xinto.opencord.ui.component.layout.OpenCordBackground
import com.xinto.opencord.ui.component.text.OpenCordText
import com.xinto.opencord.ui.viewmodel.MainViewModel
import com.xinto.opencord.ui.widgets.chat.WidgetChatMessage
import com.xinto.opencord.ui.widgets.textfield.ChannelTextField

@Composable
fun CenterPanel(viewModel: MainViewModel) {
    val messagesResult by viewModel.currentChannelMessages.collectAsState()
    val currentChannel by viewModel.currentChannel.collectAsState()

    var message by remember(currentChannel) { mutableStateOf("") }

    OpenCordBackground(
        modifier = Modifier.fillMaxSize()
    ) {
        Crossfade(targetState = messagesResult) { messagesResult ->
            when (val result = messagesResult) {
                is DiscordAPIResult.Loading -> {
                    CircularProgressIndicator()
                }
                is DiscordAPIResult.Success -> {
                    Column {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            reverseLayout = true
                        ) {
                            items(result.data) { message ->
                                WidgetChatMessage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    message = message
                                )
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
                is DiscordAPIResult.Error -> {
                    OpenCordText(text = "Failed to load data")
                }
            }
        }
    }
}