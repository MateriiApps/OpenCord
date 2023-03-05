package com.xinto.opencord.ui.screens.home.panels.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.size.Size
import com.xinto.opencord.R
import com.xinto.opencord.domain.attachment.DomainPictureAttachment
import com.xinto.opencord.domain.attachment.DomainVideoAttachment
import com.xinto.opencord.domain.emoji.*
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.DomainMessageRegular
import com.xinto.opencord.ui.components.OCAsyncImage
import com.xinto.opencord.ui.components.attachment.AttachmentPicture
import com.xinto.opencord.ui.components.attachment.AttachmentVideo
import com.xinto.opencord.ui.components.channel.ChatInput
import com.xinto.opencord.ui.components.embed.Embed
import com.xinto.opencord.ui.components.embed.EmbedAuthor
import com.xinto.opencord.ui.components.embed.EmbedField
import com.xinto.opencord.ui.components.message.*
import com.xinto.opencord.ui.components.message.reply.MessageReferenced
import com.xinto.opencord.ui.components.message.reply.MessageReferencedAuthor
import com.xinto.opencord.ui.components.message.reply.MessageReferencedContent
import com.xinto.opencord.ui.util.ContentAlpha
import com.xinto.opencord.ui.util.ProvideContentAlpha
import com.xinto.opencord.ui.viewmodel.ChatViewModel
import com.xinto.opencord.util.ifComposable
import com.xinto.opencord.util.ifNotEmptyComposable
import com.xinto.opencord.util.ifNotNullComposable
import com.xinto.simpleast.render
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
fun ChatLoaded(
    messages: ImmutableList<DomainMessage>,
    reactions: SnapshotStateMap<Long, SnapshotStateMap<DomainEmojiIdentifier, ChatViewModel.ReactionState>>,
    currentUserId: Long?,
    channelName: String,
    userMessage: String,
    sendEnabled: Boolean,
    onUserMessageUpdate: (String) -> Unit,
    onUserMessageSend: () -> Unit,
    onMessageReact: (messageId: Long, emoji: DomainEmoji) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState() // TODO: scroll to target message if jumping

    val coroutineScope = rememberCoroutineScope()
    val messageMenuState = rememberSheetState()
    var messageMenuTarget by remember { mutableStateOf<DomainMessage?>(null) }

    LaunchedEffect(messages.size) {
        if (listState.firstVisibleItemIndex <= 1) {
            listState.animateScrollToItem(0)
        }
    }

    if (messageMenuState.isVisible || messageMenuState.targetValue != SheetValue.Hidden) {
        BackHandler {
            coroutineScope.launch {
                messageMenuState.hide()
                messageMenuTarget = null
            }
        }

        ModalBottomSheet(
            sheetState = messageMenuState,
            onDismissRequest = {
                coroutineScope.launch {
                    messageMenuState.hide()
                    messageMenuTarget = null
                }
            },
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp),
            ) {
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                ) {
                    when (val message = messageMenuTarget) {
                        is DomainMessageRegular -> {
                            MessageRegular(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.medium)
                                    .clickable { },
                                mentioned = false,
                                reply = message.isReply.ifComposable {
                                    val referencedMessage = message.referencedMessage
                                    if (referencedMessage != null) {
                                        MessageReferenced(
                                            avatar = {
                                                MessageAvatar(url = referencedMessage.author.avatarUrl)
                                            },
                                            author = {
                                                MessageReferencedAuthor(author = referencedMessage.author.username)
                                            },
                                            content = {
                                                MessageReferencedContent(
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
                                avatar = {
                                    MessageAvatar(url = message.author.avatarUrl)
                                },
                                author = {
                                    MessageAuthor(
                                        author = message.author.username,
                                        timestamp = message.formattedTimestamp,
                                        isEdited = message.isEdited,
                                        isBot = message.author.bot,
                                    )
                                },
                                content = {
                                    if (message.contentNodes.isNotEmpty()) {
                                        MessageContent(
                                            text = render(
                                                nodes = message.contentNodes,
                                                renderContext = null,
                                            ).toAnnotatedString(),
                                        )
                                    } else {
                                        ProvideContentAlpha(ContentAlpha.high) {
                                            ProvideTextStyle(MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)) {
                                                if (message.attachments.isNotEmpty()) {
                                                    Text("${message.attachments.size} attachments")
                                                } else if (message.embeds.isNotEmpty()) {
                                                    Text("${message.embeds.size} embeds")
                                                }
                                            }
                                        }
                                    }
                                },
                            )
                        }
                        // TODO: render other message types
                        else -> {}
                    }
                }

                Divider(
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                ) {
                    val emojis = arrayOf(
                        "\uD83D\uDDFF",
                        "\uD83D\uDC80",
                        "\uD83D\uDE2D",
                        "\uD83D\uDE15",
                        "\uD83D\uDE44",
                        "⭐",
                    )

                    for (emoji in emojis) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.extraLarge,
                            modifier = Modifier
                                .size(42.dp),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { /* TODO: react to message */ },
                            ) {
                                Text(
                                    text = emoji,
                                    fontSize = 20.sp,
                                )
                            }
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier
                            .size(42.dp),
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { /* TODO: reaction selector sheet */ },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_add_reaction),
                                contentDescription = "Open reaction picker",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .padding(6.dp),
                            )
                        }
                    }
                }

                val messageMenuItems = arrayOf(
                    "Reply" to R.drawable.ic_reply,
                    "Edit" to R.drawable.ic_edit,
                    "Delete" to R.drawable.ic_delete,
                    "Copy Message Link" to R.drawable.ic_link,
                    "Copy Message" to R.drawable.ic_file_copy,
                    "Mark Unread" to R.drawable.ic_mark_unread,
                    "Pin" to R.drawable.ic_pin,
                    "Copy ID" to R.drawable.ic_file_copy,
                )

                for ((name, icon) in messageMenuItems) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(horizontal = 14.dp, vertical = 13.dp),
                        ) {
                            Icon(
                                painter = painterResource(icon),
                                contentDescription = null,
                                modifier = Modifier.size(25.dp),
                            )

                            Text(
                                text = name,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.fillMaxHeight())
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

                        val messageReactions by remember(reactions[message.id]) {
                            derivedStateOf {
                                reactions[message.id]?.values
                                    ?.sortedBy { it.reactionOrder }
                                    ?.toImmutableList()
                            }
                        }

                        MessageRegular(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = {
                                        messageMenuTarget = message
                                        coroutineScope.launch {
                                            messageMenuState.show()
                                        }
                                    },
                                ),
                            mentioned = mentioned,
                            reply = message.isReply.ifComposable {
                                val referencedMessage = message.referencedMessage
                                if (referencedMessage != null) {
                                    MessageReferenced(
                                        avatar = {
                                            MessageAvatar(url = referencedMessage.author.avatarUrl)
                                        },
                                        author = {
                                            MessageReferencedAuthor(author = referencedMessage.author.username)
                                        },
                                        content = {
                                            MessageReferencedContent(
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
                                MessageAvatar(url = message.author.avatarUrl)
                            },
                            author = if (canMerge) null else { ->
                                MessageAuthor(
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
                                MessageContent(
                                    text = render(
                                        nodes = nodes,
                                        renderContext = null,
                                    ).toAnnotatedString(),
                                )
                            },
                            embeds = message.embeds.ifNotEmptyComposable { embeds ->
                                for (embed in embeds) {
                                    Embed(
                                        title = embed.title,
                                        description = embed.description,
                                        color = embed.color,
                                        author = embed.author.ifNotNullComposable { EmbedAuthor(name = it) },
                                        fields = embed.fields.ifNotNullComposable {
                                            for (field in it) {
                                                EmbedField(
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
                                        is DomainPictureAttachment -> {
                                            AttachmentPicture(
                                                modifier = Modifier
                                                    .heightIn(max = 400.dp),
                                                url = attachment.proxyUrl,
                                                width = attachment.width,
                                                height = attachment.height,
                                            )
                                        }
                                        is DomainVideoAttachment -> {
                                            AttachmentVideo(
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
                            reactions = messageReactions?.ifNotEmptyComposable { reactions ->
                                for (reaction in reactions) {
                                    MessageReaction(
                                        onClick = {
                                            onMessageReact(message.id, reaction.emoji)
                                        },
                                        count = reaction.count,
                                        meReacted = reaction.meReacted,
                                    ) {
                                        when (reaction.emoji) {
                                            is DomainUnicodeEmoji -> {
                                                Text(
                                                    text = reaction.emoji.emoji,
                                                    fontSize = 16.sp,
                                                )
                                            }
                                            is DomainGuildEmoji -> {
                                                OCAsyncImage(
                                                    url = reaction.emoji.url,
                                                    size = Size(64, 64),
                                                    modifier = Modifier
                                                        .size(18.dp),
                                                )
                                            }
                                            is DomainUnknownEmoji -> {}
                                        }
                                    }
                                }
                            },
                        )
                    }
                    // TODO: render other message types
                    else -> {}
                }
            }
        }
        ChatInput(
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
