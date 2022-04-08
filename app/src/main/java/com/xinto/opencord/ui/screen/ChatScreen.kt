package com.xinto.opencord.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.xinto.bdc.BottomSheetDialog
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.ui.component.OCTopAppBar
import com.xinto.opencord.ui.viewmodel.ChatViewModel
import com.xinto.opencord.ui.widget.WidgetChatInput
import com.xinto.opencord.ui.widget.WidgetChatMessage
import com.xinto.opencord.util.SimpleAstParser
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

@ExperimentalFoundationApi
@Composable
fun ChatScreen(
    onChannelsButtonClick: () -> Unit,
    onMembersButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    parser: SimpleAstParser = get(),
    viewModel: ChatViewModel = getViewModel(),
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            OCTopAppBar(
                title = {
                    Text(viewModel.channelName)
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
            WidgetChatInput(
                value = viewModel.userMessage,
                onValueChange = {
                    viewModel.updateMessage(it)
                },
                onSendClick = {
                    viewModel.sendMessage()
                }
            )
        }
    ) {
        when (viewModel.state) {
            is ChatViewModel.State.Loading -> {
                ChatScreenLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                )
            }
            is ChatViewModel.State.Loaded -> {
                ChatScreenLoaded(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    parser = parser,
                    messages = viewModel.messages
                )
            }
            is ChatViewModel.State.Error -> {

            }
        }
    }
}

@Composable
fun ChatScreenLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ChatScreenLoaded(
    messages: List<DomainMessage>,
    parser: SimpleAstParser,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        reverseLayout = true,
    ) {
        items(messages) { message ->
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
                MessageActionMenu(
                    onDismissRequest = {
                        showBottomDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun MessageActionMenu(
    onDismissRequest: () -> Unit
) {
    BottomSheetDialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface {
            Column {
//                FullWidthButton(
//                    modifier = Modifier.fillMaxWidth(),
//                    icon = {},
//                    content = {
//                        Text("Reply")
//                    }
//                ) {
//
//                }
//                FullWidthButton(
//                    modifier = Modifier.fillMaxWidth(),
//                    icon = {},
//                    content = {
//                        Text("Create Thread")
//                    }
//                ) {
//
//                }
//                FullWidthButton(
//                    modifier = Modifier.fillMaxWidth(),
//                    icon = {},
//                    content = {
//                        Text("Copy Text")
//                    }
//                ) {
//
//                }
//                FullWidthButton(
//                    modifier = Modifier.fillMaxWidth(),
//                    icon = {},
//                    content = {
//                        Text("Delete")
//                    }
//                ) {
//
//                }
            }
        }
    }
}