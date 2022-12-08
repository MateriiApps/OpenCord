package com.xinto.opencord.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.xinto.bdc.BottomSheetDialog
import com.xinto.opencord.R
import com.xinto.opencord.domain.model.DomainAttachment
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.domain.model.DomainMessageRegular
import com.xinto.opencord.ui.viewmodel.ChatViewModel
import com.xinto.opencord.ui.widget.*
import com.xinto.opencord.util.ifComposable
import com.xinto.opencord.util.ifNotEmptyComposable
import com.xinto.opencord.util.ifNotNullComposable
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
    val sortedMessages by remember(viewModel.messages) {
        derivedStateOf {
            viewModel.getSortedMessages()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SmallTopAppBar(
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
                    ChatScreenUnselected(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is ChatViewModel.State.Loading -> {
                    ChatScreenLoading(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is ChatViewModel.State.Loaded -> {
                    ChatScreenLoaded(
                        messages = sortedMessages,
                        currentUserId = viewModel.currentUserId,
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
                        modifier = Modifier.fillMaxSize(),
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
        contentAlignment = Alignment.Center,
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
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .verticalScroll(
                state = rememberScrollState(),
                enabled = false,
            ),
    )
    {
        repeat(10) {
            WidgetChatMessage(
                modifier = Modifier.fillMaxWidth(),
                avatar = {
                    Box(
                        modifier = Modifier
                            .shimmer(shimmer)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)),
                    )
                },
                author = {
                    val width = remember { (30..100).random().dp }
                    Box(
                        modifier = Modifier
                            .shimmer(shimmer)
                            .size(width = width, height = 14.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
                    )
                },
                content = {
                    val rowCount = remember { (1..3).random() }
                    repeat(rowCount) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            val itemCount = (1..5).random()
                            repeat(itemCount) {
                                val spaces = remember { (10..30).random() }
                                Text(
                                    text = " ".repeat(spaces),
                                    modifier = Modifier
                                        .shimmer(shimmer)
                                        .padding(top = 8.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)),
                                )
                            }
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun ChatScreenLoaded(
    messages: List<DomainMessage>,
    currentUserId: Long?,
    channelName: String,
    userMessage: String,
    sendEnabled: Boolean,
    onUserMessageUpdate: (String) -> Unit,
    onUserMessageSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: scroll to target message if jumping
    val listState = rememberLazyListState()

    // TODO: toggleable auto scroll by scrolling up
    LaunchedEffect(messages.size) {
        if (listState.firstVisibleItemIndex <= 1) {
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            reverseLayout = true,
        ) {
            itemsIndexed(messages, key = { _, m -> m.id }) { i, message ->
                var showBottomDialog by rememberSaveable { mutableStateOf(false) }
                val mentioned by remember {
                    derivedStateOf {
                        if (message !is DomainMessageRegular) false
                        else {
                            message.mentions.any { it.id == currentUserId }
                                    || message.mentionEveryone
                        }
                    }
                }

                when (message) {
                    is DomainMessageRegular -> {
                        val prevMessage = messages.getOrNull(i + 1)
                        val canMerge = prevMessage != null
                                && prevMessage is DomainMessageRegular
                                && message.author.id == prevMessage.author.id
                                && (message.timestamp - prevMessage.timestamp).inWholeMinutes < 1
                                && message.attachments.isEmpty()
                                && prevMessage.attachments.isEmpty()
                                && message.embeds.isEmpty()
                                && prevMessage.embeds.isEmpty()
                                && !message.isReply
                                && !prevMessage.isReply

                        WidgetChatMessage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = { showBottomDialog = true },
                                ),
                            mentioned = mentioned,
                            reply = message.isReply.ifComposable {
                                val referencedMessage = message.referencedMessage
                                if (referencedMessage != null) {
                                    WidgetMessageReply(
                                        avatar = {
                                            WidgetMessageAvatar(url = referencedMessage.author.avatarUrl)
                                        },
                                        author = {
                                            WidgetMessageReplyAuthor(author = referencedMessage.author.username)
                                        },
                                        content = {
                                            WidgetMessageReplyContent(
                                                text = render(
                                                    nodes = referencedMessage.contentNodes,
                                                    renderContext = null,
                                                ).toAnnotatedString(),
                                            )
                                        },
                                    )
                                } else {
                                    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                                        Text(stringResource(R.string.message_reply_unknown))
                                    }
                                }
                            },
                            avatar = if (canMerge) null else { ->
                                WidgetMessageAvatar(url = message.author.avatarUrl)
                            },
                            author = if (canMerge) null else { ->
                                WidgetMessageAuthor(
                                    author = message.author.username,
                                    timestamp = message.formattedTimestamp,
                                    isEdited = message.isEdited,
                                    isBot = message.author.bot,
                                    onAuthorClick = {
                                        onUserMessageUpdate("$userMessage${message.author.formattedMention} ")
                                    },
                                )
                            },
                            content = message.contentNodes.ifNotEmptyComposable { nodes ->
                                WidgetMessageContent(
                                    text = render(
                                        nodes = nodes,
                                        renderContext = null,
                                    ).toAnnotatedString(),
                                )
                            },
                            embeds = message.embeds.ifNotEmptyComposable { embeds ->
                                for (embed in embeds) {
                                    WidgetEmbed(
                                        title = embed.title,
                                        description = embed.description,
                                        color = embed.color,
                                        author = embed.author.ifNotNullComposable {
                                            WidgetEmbedAuthor(name = it.name)
                                        },
                                        fields = embed.fields.ifNotNullComposable {
                                            for (field in it) {
                                                WidgetEmbedField(
                                                    name = field.name,
                                                    value = field.value,
                                                )
                                            }
                                        },
                                    )
                                }
                            },
                            attachments = message.attachments.ifNotEmptyComposable { attachments ->
                                for (attachment in attachments) {
                                    when (attachment) {
                                        is DomainAttachment.Picture -> {
                                            WidgetAttachmentPicture(
                                                modifier = Modifier
                                                    .heightIn(max = 400.dp),
                                                url = attachment.proxyUrl,
                                                width = attachment.width,
                                                height = attachment.height,
                                            )
                                        }
                                        is DomainAttachment.Video -> {
                                            WidgetAttachmentVideo(
                                                url = attachment.url,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .aspectRatio(attachment.width.toFloat() / attachment.height.toFloat()),
                                            )
                                        }
                                        else -> {}
                                    }
                                }
                            },
                        )
                    }
                    else -> {}
                }
                if (showBottomDialog) {
                    MessageActionMenu(
                        onDismissRequest = { showBottomDialog = false },
                    )
                }
            }
        }
        WidgetChatInput(
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 4.dp,
            ),
            value = userMessage,
            onValueChange = onUserMessageUpdate,
            sendEnabled = sendEnabled,
            onSendClick = onUserMessageSend,
            hint = { Text(stringResource(R.string.chat_input_hint, channelName)) },
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
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.error,
            LocalTextStyle provides MaterialTheme.typography.titleMedium,
        ) {
            Icon(
                modifier = Modifier.size(56.dp),
                painter = painterResource(R.drawable.ic_error),
                contentDescription = null,
            )
            Text(
                text = stringResource(R.string.chat_loading_error),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun MessageActionMenu(
    onDismissRequest: () -> Unit
) {
    BottomSheetDialog(
        onDismissRequest = onDismissRequest,
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
