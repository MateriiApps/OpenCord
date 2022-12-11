package com.xinto.opencord.ui.screens.home.panels.chat

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.attachment.DomainPictureAttachment
import com.xinto.opencord.domain.attachment.DomainVideoAttachment
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.DomainMessageRegular
import com.xinto.opencord.ui.components.attachment.AttachmentPicture
import com.xinto.opencord.ui.components.attachment.AttachmentVideo
import com.xinto.opencord.ui.components.channel.ChatInput
import com.xinto.opencord.ui.components.embed.Embed
import com.xinto.opencord.ui.components.embed.EmbedAuthor
import com.xinto.opencord.ui.components.embed.EmbedField
import com.xinto.opencord.ui.components.message.MessageAuthor
import com.xinto.opencord.ui.components.message.MessageAvatar
import com.xinto.opencord.ui.components.message.MessageContent
import com.xinto.opencord.ui.components.message.MessageRegular
import com.xinto.opencord.ui.components.message.reply.MessageReferenced
import com.xinto.opencord.ui.components.message.reply.MessageReferencedAuthor
import com.xinto.opencord.ui.components.message.reply.MessageReferencedContent
import com.xinto.opencord.util.ifComposable
import com.xinto.opencord.util.ifNotEmptyComposable
import com.xinto.opencord.util.ifNotNullComposable
import com.xinto.simpleast.render
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ChatLoaded(
    messages: ImmutableList<DomainMessage>,
    currentUserId: Long?,
    channelName: String,
    userMessage: String,
    sendEnabled: Boolean,
    onUserMessageUpdate: (String) -> Unit,
    onUserMessageSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMessageMenu by rememberSaveable { mutableStateOf(false) }

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

                        MessageRegular(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = { showMessageMenu = true },
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
                                        author = embed.author.ifNotNullComposable {
                                            EmbedAuthor(name = it.name)
                                        },
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
                        )
                    }
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
