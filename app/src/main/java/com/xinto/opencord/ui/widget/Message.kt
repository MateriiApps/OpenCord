package com.xinto.opencord.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.R
import com.xinto.opencord.domain.model.DomainAttachment
import com.xinto.opencord.domain.model.DomainEmbed
import com.xinto.opencord.ui.component.rememberOCCoilPainter
import com.xinto.opencord.util.letComposable

@Composable
fun WidgetChatMessage(
    avatar: Painter,
    author: String,
    timestamp: String,
    message: AnnotatedString,
    attachments: List<DomainAttachment>,
    embeds: List<DomainEmbed>,
    edited: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .heightIn(min = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                painter = avatar,
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProvideTextStyle(MaterialTheme.typography.labelLarge) {
                        Text(author)
                    }
                    CompositionLocalProvider(
                        LocalContentColor provides LocalContentColor.current.copy(alpha = 0.7f)
                    ) {
                        ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                            Text("·")
                        }
                        ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                            Text(timestamp)
                        }
                        if (edited) {
                            ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                                Text("·")
                            }
                            Icon(
                                modifier = Modifier
                                    .size(12.dp),
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = null,
                            )
                        }
                    }
                }
                if (message.isNotEmpty()) {
                    WidgetMessageContent(
                        modifier = Modifier.fillMaxWidth(),
                        text = message
                    )
                }
                if (attachments.isNotEmpty()) {
                    WidgetMessageAttachments(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 4.dp),
                        attachments = attachments
                    )
                }
                if (embeds.isNotEmpty()) {
                    WidgetMessageEmbeds(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(vertical = 4.dp),
                        embeds = embeds
                    )
                }
            }
        }
    }
}


@Composable
private fun WidgetMessageContent(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
        Text(
            modifier = modifier,
            text = text,
            inlineContent = mapOf(
                "emote" to InlineTextContent(
                    placeholder = Placeholder(
                        width = 20.sp,
                        height = 20.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) { emoteId ->
                    val image = rememberOCCoilPainter("${BuildConfig.URL_CDN}/emojis/$emoteId")
                    Image(
                        painter = image,
                        contentDescription = "Emote"
                    )
                }
            )
        )
    }
}

@Composable
private fun WidgetMessageAttachments(
    attachments: List<DomainAttachment>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (attachment in attachments) {
            when (attachment) {
                is DomainAttachment.Picture -> {
                    WidgetMediaPicture(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.medium)
                            .heightIn(max = 350.dp),
                        imageUrl = attachment.url,
                        imageWidth = attachment.width,
                        imageHeight = attachment.height
                    )
                }
                is DomainAttachment.Video -> {
                    WidgetMediaVideo(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium),
                        videoUrl = attachment.url,
                    )
                }
                else -> { /* TODO */
                }
            }
        }
    }
}

@Composable
private fun WidgetMessageEmbeds(
    embeds: List<DomainEmbed>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (embed in embeds) {
            WidgetEmbed(
                title = embed.title,
                description = embed.description,
                color = embed.color,
                fields = embed.fields?.letComposable { fields ->
                    for (field in fields) {
                        WidgetEmbedField(
                            name = field.name,
                            value = field.value
                        )
                    }
                },
                author = embed.author?.letComposable { author ->
                    WidgetEmbedAuthor(
                        name = author.name
                    )
                }
            )
        }
    }
}