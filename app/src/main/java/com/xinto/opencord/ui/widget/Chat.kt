package com.xinto.opencord.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.model.DomainAttachment
import com.xinto.opencord.ui.component.FilledIconButton
import com.xinto.opencord.ui.component.OCBasicTextField
import com.xinto.opencord.ui.component.rememberOCCoilPainter

@Composable
fun WidgetChatMessage(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    avatar: Painter,
    author: String,
    timestamp: String,
    message: AnnotatedString,
    attachments: List<DomainAttachment>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
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
                    .weight(1f)
                    .padding(vertical = 2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val alphaContentColor = LocalContentColor.current.copy(alpha = 0.7f)
                    ProvideTextStyle(MaterialTheme.typography.labelLarge) {
                        Text(author)
                    }
                    ContentAlpha.medium
                    ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                        Text("·", color = alphaContentColor)
                    }
                    ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                        Text(timestamp, color = alphaContentColor)
                    }
                }
                if (message.isNotEmpty()) {
                    WidgetMessageContent(
                        modifier = Modifier.fillMaxWidth(),
                        text = message
                    )
                }
                WidgetMessageAttachments(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    attachments = attachments
                )
            }
        }
    }
}

@Composable
fun WidgetChatInput(
    value: String,
    onValueChange: (value: String) -> Unit,
    sendEnabled: Boolean,
    onSendClick: () -> Unit,
    hint: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OCBasicTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            hint = hint,
            maxLines = 4,
        )
        AnimatedVisibility(
            modifier = Modifier.size(48.dp),
            visible = value.isNotBlank(),
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it },
        ) {
            FilledIconButton(
                onClick = onSendClick,
                enabled = sendEnabled
            ) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun WidgetMessageContent(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    ProvideTextStyle(MaterialTheme.typography.bodyMedium.copy(letterSpacing = 0.sp)) {
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
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium),
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