package com.xinto.opencord.ui.screens.pins

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.attachment.DomainPictureAttachment
import com.xinto.opencord.domain.attachment.DomainVideoAttachment
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.DomainMessageRegular
import com.xinto.opencord.ui.components.OCImage
import com.xinto.opencord.ui.components.OCSize
import com.xinto.opencord.ui.components.attachment.AttachmentPicture
import com.xinto.opencord.ui.components.attachment.AttachmentVideo
import com.xinto.opencord.ui.components.embed.*
import com.xinto.opencord.ui.components.message.MessageAuthor
import com.xinto.opencord.ui.components.message.MessageAvatar
import com.xinto.opencord.ui.components.message.MessageContent
import com.xinto.opencord.ui.components.message.MessageRegular
import com.xinto.opencord.ui.components.message.reply.MessageReferenced
import com.xinto.opencord.ui.components.message.reply.MessageReferencedAuthor
import com.xinto.opencord.ui.components.message.reply.MessageReferencedContent
import com.xinto.opencord.ui.screens.home.panels.messagemenu.MessageMenu
import com.xinto.opencord.ui.util.CompositePaddingValues
import com.xinto.opencord.ui.util.ifComposable
import com.xinto.opencord.ui.util.ifNotEmptyComposable
import com.xinto.opencord.ui.util.ifNotNullComposable
import kotlinx.collections.immutable.ImmutableList

@Composable
fun PinsScreenLoaded(
    contentPadding: PaddingValues,
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
        contentPadding = CompositePaddingValues(
            contentPadding,
            PaddingValues(
                horizontal = 10.dp,
                vertical = 4.dp,
            ),
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
                            onLongClick = { messageMenuTarget = message.id },
                            modifier = Modifier
                                .fillMaxWidth()
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
                                        content = referencedMessage.contentRendered.ifNotEmptyComposable {
                                            MessageReferencedContent(
                                                text = referencedMessage.contentRendered,
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
                            content = message.contentRendered.ifNotEmptyComposable {
                                MessageContent(
                                    text = message.contentRendered,
                                )
                            },
                            embeds = message.embeds.ifNotEmptyComposable { embeds ->
                                val renderedEmbeds = if (message.isTwitterMultiImageMessage) listOf(embeds.first()) else embeds

                                for (embed in renderedEmbeds) key(embed) {
                                    if (embed.isVideoOnlyEmbed) {
                                        val video = embed.video!!
                                        AttachmentVideo(
                                            url = video.proxyUrl!!,
                                            modifier = Modifier
                                                .heightIn(max = 400.dp)
                                                .aspectRatio(
                                                    ratio = video.aspectRatio,
                                                    matchHeightConstraintsFirst = true,
                                                ),
                                        )
                                    } else if (embed.isSpotifyEmbed) {
                                        SpotifyEmbed(
                                            embedUrl = embed.spotifyEmbedUrl!!,
                                            isSpotifyTrack = embed.isSpotifyTrack,
                                        )
                                    } else {
                                        Embed(
                                            title = embed.title,
                                            url = embed.url,
                                            description = embed.description,
                                            color = embed.color,
                                            author = embed.author.ifNotNullComposable {
                                                EmbedAuthor(
                                                    name = it.name,
                                                    url = it.url,
                                                    iconUrl = it.iconUrl,
                                                )
                                            },
                                            media = if (!message.isTwitterMultiImageMessage) {
                                                embed.image.ifNotNullComposable {
                                                    AttachmentPicture(
                                                        url = it.sizedUrl,
                                                        width = it.width ?: 500,
                                                        height = it.height ?: 500,
                                                        modifier = Modifier
                                                            .heightIn(max = 400.dp),
                                                    )
                                                } ?: embed.video.ifNotNullComposable {
                                                    EmbedVideo(
                                                        video = it,
                                                        videoPublicUrl = embed.url,
                                                        thumbnail = embed.thumbnail,
                                                    )
                                                }
                                            } else {
                                                {
                                                    @OptIn(ExperimentalLayoutApi::class)
                                                    FlowRow(
                                                        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally),
                                                        maxItemsInEachRow = 2,
                                                        modifier = Modifier
                                                            .clip(MaterialTheme.shapes.small),
                                                    ) {
                                                        for ((i, twitterEmbed) in embeds.withIndex()) key(twitterEmbed.image) {
                                                            val image = twitterEmbed.image!!
                                                            val isLastRow = i >= embeds.size - 2 // needed or parent clipping breaks

                                                            OCImage(
                                                                url = image.sizedUrl,
                                                                size = OCSize(image.width ?: 500, image.height ?: 500),
                                                                contentScale = ContentScale.FillWidth,
                                                                modifier = Modifier
                                                                    .fillMaxWidth(0.48f)
                                                                    .heightIn(max = 350.dp)
                                                                    .padding(bottom = if (isLastRow) 0.dp else 5.dp),
                                                            )
                                                        }
                                                    }
                                                }
                                            },
                                            thumbnail = embed.thumbnail.ifNotNullComposable {
                                                AttachmentPicture(
                                                    url = it.sizedUrl,
                                                    width = it.width ?: 256,
                                                    height = it.height ?: 256,
                                                    modifier = Modifier
                                                        .size(45.dp),
                                                )
                                            },
                                            fields = embed.fields.ifNotNullComposable {
                                                for (field in it) key(field) {
                                                    EmbedField(
                                                        name = field.name,
                                                        value = field.value,
                                                    )
                                                }
                                            },
                                            footer = embed.footer.ifNotNullComposable {
                                                EmbedFooter(
                                                    text = it.text,
                                                    iconUrl = it.displayUrl,
                                                    timestamp = it.formattedTimestamp,
                                                )
                                            },
                                        )
                                    }
                                }
                            },
                            attachments = message.attachments.ifNotEmptyComposable { attachments ->
                                for (attachment in attachments) key(attachment) {
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
