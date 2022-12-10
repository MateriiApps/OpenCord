package com.xinto.opencord.ui.screen.pins

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import com.xinto.opencord.domain.model.DomainAttachment
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.domain.model.DomainMessageRegular
import com.xinto.opencord.ui.component.attachment.AttachmentPicture
import com.xinto.opencord.ui.component.attachment.AttachmentVideo
import com.xinto.opencord.ui.component.embed.Embed
import com.xinto.opencord.ui.component.embed.EmbedAuthor
import com.xinto.opencord.ui.component.embed.EmbedField
import com.xinto.opencord.ui.component.message.MessageRegular
import com.xinto.opencord.ui.component.message.reply.MessageRepliedToContent
import com.xinto.opencord.ui.component.message.reply.MessageReply
import com.xinto.opencord.ui.component.message.MessageAuthor
import com.xinto.opencord.ui.component.message.MessageAvatar
import com.xinto.opencord.ui.component.message.MessageContent
import com.xinto.opencord.ui.component.message.reply.MessageReferencedAuthor
import com.xinto.opencord.util.ifComposable
import com.xinto.opencord.util.ifNotEmptyComposable
import com.xinto.opencord.util.ifNotNullComposable
import com.xinto.simpleast.render

@Composable
fun PinsScreenLoaded(
    pins: List<DomainMessage>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
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
                            modifier = Modifier.fillMaxWidth(),
                            reply = message.isReply.ifComposable {
                                val referencedMessage = message.referencedMessage
                                if (referencedMessage != null) {
                                    MessageReply(
                                        avatar = {
                                            MessageAvatar(url = referencedMessage.author.avatarUrl)
                                        },
                                        author = {
                                            MessageReferencedAuthor(author = referencedMessage.author.username)
                                        },
                                        content = {
                                            MessageRepliedToContent(
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
                                        is DomainAttachment.Picture -> {
                                            AttachmentPicture(
                                                modifier = Modifier
                                                    .heightIn(max = 250.dp),
                                                url = attachment.proxyUrl,
                                                width = attachment.width,
                                                height = attachment.height,
                                            )
                                        }
                                        is DomainAttachment.Video -> {
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
