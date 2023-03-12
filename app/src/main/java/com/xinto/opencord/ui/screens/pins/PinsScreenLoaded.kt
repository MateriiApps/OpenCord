package com.xinto.opencord.ui.screens.pins

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.attachment.DomainPictureAttachment
import com.xinto.opencord.domain.attachment.DomainVideoAttachment
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.DomainMessageRegular
import com.xinto.opencord.ui.components.attachment.AttachmentPicture
import com.xinto.opencord.ui.components.attachment.AttachmentVideo
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
import com.xinto.opencord.ui.screens.home.panels.messagemenu.MessageMenu
import com.xinto.opencord.util.ifComposable
import com.xinto.opencord.util.ifNotEmptyComposable
import com.xinto.opencord.util.ifNotNullComposable
import com.xinto.simpleast.render
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PinsScreenLoaded(
    pins: ImmutableList<DomainMessage>,
    modifier: Modifier = Modifier,
) {
    var messageMenuTarget by remember { mutableStateOf<Long?>(null) }

    if (messageMenuTarget != null) {
        MessageMenu(
            messageId = messageMenuTarget!!,
            onDismiss = { messageMenuTarget = null },
        )
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            start = 10.dp,
            end = 10.dp,
            top = 4.dp,
            bottom = 10.dp,
        ),
    ) {
        items(pins) { message ->
            when (message) {
                is DomainMessageRegular -> {
                    Surface(
                        modifier = Modifier.fillParentMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 1.dp,
                    ) {
                        MessageRegular(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = { messageMenuTarget = message.id },
                                )
                                .padding(8.dp),
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
                            content = message.contentNodes.ifNotEmptyComposable { nodes ->
                                MessageContent(
                                    text = render(
                                        builder = AnnotatedString.Builder(),
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
                                                    .heightIn(max = 250.dp),
                                                url = attachment.proxyUrl,
                                                width = attachment.width,
                                                height = attachment.height,
                                            )
                                        }
                                        is DomainVideoAttachment -> {
                                            AttachmentVideo(url = attachment.url)
                                        }
                                        else -> {}
                                    }
                                }
                            },
                        )
                    }
                }
                else -> {}
            }
        }
    }
}
