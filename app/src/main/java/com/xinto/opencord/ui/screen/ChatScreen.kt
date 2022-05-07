package com.xinto.opencord.ui.screen

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.xinto.bdc.BottomSheetDialog
import com.xinto.opencord.R
import com.xinto.opencord.domain.model.DomainAttachment
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.ui.viewmodel.ChatViewModel
import com.xinto.opencord.ui.widget.*
import com.xinto.opencord.util.letComposable
import com.xinto.simpleast.render
import org.koin.androidx.compose.getViewModel

@Composable
fun ChatScreen(
    onChannelsButtonClick: () -> Unit,
    onMembersButtonClick: () -> Unit,
    onPinsButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = getViewModel(),
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SmallTopAppBar(
                title = { Text(stringResource(R.string.chat_title, viewModel.channelName)) },
                navigationIcon = {
                    IconButton(onChannelsButtonClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_menu),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onPinsButtonClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_push_pin),
                            contentDescription = null
                        )
                    }
                    IconButton(onMembersButtonClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_people),
                            contentDescription = null
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
            tonalElevation = 2.dp
        ) {
            when (viewModel.state) {
                is ChatViewModel.State.Unselected -> {
                    ChatScreenUnselected(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is ChatViewModel.State.Loading -> {
                    ChatScreenLoading(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is ChatViewModel.State.Loaded -> {
                    ChatScreenLoaded(
                        messages = viewModel.messages.values.sortedByDescending {
                            it.timestamp
                        },
                        channelName = viewModel.channelName,
                        userMessage = viewModel.userMessage,
                        sendEnabled = viewModel.sendEnabled,
                        onUserMessageUpdate = viewModel::updateMessage,
                        onUserMessageSend = viewModel::sendMessage,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is ChatViewModel.State.Error -> {
                    ChatScreenError(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatScreenUnselected(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        ProvideTextStyle(MaterialTheme.typography.titleMedium) {
            Text(stringResource(R.string.chat_unselected_message))
        }
    }
}

@Composable
private fun ChatScreenLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ChatScreenLoaded(
    messages: List<DomainMessage>,
    channelName: String,
    userMessage: String,
    sendEnabled: Boolean,
    onUserMessageUpdate: (String) -> Unit,
    onUserMessageSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true,
        ) {
            items(messages) { message ->
                var showBottomDialog by rememberSaveable { mutableStateOf(false) }
                WidgetChatMessage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .combinedClickable(
                            onClick = {},
                            onLongClick = { showBottomDialog = true }
                        ),
                    avatar = {
                        WidgetMessageAvatar(url = message.author.avatarUrl)
                    },
                    author = {
                        WidgetMessageAuthor(
                            author = message.author.username,
                            timestamp = message.formattedTimestamp,
                            edited = message.isEdited,
                            onAuthorClick = {
                                onUserMessageUpdate("$userMessage${message.author.formattedMention} ")
                            },
                        )
                    },
                    content = message.contentNodes.ifEmpty { null }?.letComposable { nodes ->
                        WidgetMessageContent(
                            text = render(
                                builder = AnnotatedString.Builder(),
                                nodes = nodes,
                                renderContext = null
                            ).toAnnotatedString()
                        )
                    },
                    embeds = message.embeds.ifEmpty { null }?.letComposable { embeds ->
                        for (embed in embeds) {
                            WidgetEmbed(
                                title = embed.title,
                                description = embed.description,
                                color = embed.color,
                                author = embed.author?.letComposable {
                                    WidgetEmbedAuthor(name = it.name)
                                },
                                fields = embed.fields?.letComposable {
                                    for (field in it) {
                                        WidgetEmbedField(
                                            name = field.name,
                                            value = field.value
                                        )
                                    }
                                }
                            )
                        }
                    },
                    attachments = message.attachments.ifEmpty { null }?.letComposable { attachments ->
                        for (attachment in attachments) {
                            when (attachment) {
                                is DomainAttachment.Picture -> {
                                    WidgetAttachmentPicture(
                                        modifier = Modifier
                                            .heightIn(max = 350.dp),
                                        url = attachment.proxyUrl,
                                        width = attachment.width,
                                        height = attachment.height
                                    )
                                }
                                else -> {}
                            }
                        }
                    }
                )

                if (showBottomDialog) {
                    MessageActionMenu(
                        onDismissRequest = { showBottomDialog = false }
                    )
                }
            }
        }
        WidgetChatInput(
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 4.dp
            ),
            value = userMessage,
            onValueChange = onUserMessageUpdate,
            sendEnabled = sendEnabled,
            onSendClick = onUserMessageSend,
            hint = { Text(stringResource(R.string.chat_input_hint, channelName)) }
        )
    }
}

@Composable
private fun ChatScreenError(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.error,
            LocalTextStyle provides MaterialTheme.typography.titleMedium
        ) {
            Icon(
                modifier = Modifier.size(56.dp),
                painter = painterResource(R.drawable.ic_error),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.chat_loading_error),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun MessageActionMenu(
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