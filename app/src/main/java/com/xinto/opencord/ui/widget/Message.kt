package com.xinto.opencord.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.R
import com.xinto.opencord.ui.component.OCAsyncImage
import com.xinto.opencord.ui.util.ContentAlpha
import com.xinto.opencord.ui.util.ProvideContentAlpha

@Composable
fun WidgetChatMessage(
    modifier: Modifier = Modifier,
    mentioned: Boolean = false,
    reply: (@Composable () -> Unit)? = null,
    avatar: (@Composable () -> Unit)? = null,
    author: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
    attachments: (@Composable () -> Unit)? = null,
    embeds: (@Composable () -> Unit)? = null,
) {
    val background = if (mentioned) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        Color.Unspecified
    }

    Box(modifier = modifier.background(background)) {
        Column(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (reply != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    WidgetBranchReply(
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .width(24.dp)
                            .fillMaxHeight(0.5f),
                    )
                    reply()
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top
            ) {
                if (avatar != null) {
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        avatar()
                    }
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        if (author != null) {
                            author()
                        }
                        if (content != null) {
                            ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                                content()
                            }
                        }
                    }
                    if (attachments != null) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            attachments()
                        }
                    }
                    if (embeds != null) {
                        Column(
                            modifier = Modifier.fillMaxWidth(0.9f),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            embeds()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WidgetMessageReply(
    modifier: Modifier = Modifier,
    avatar: @Composable (() -> Unit)? = null,
    author: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (avatar != null) {
            Box(
                modifier = Modifier.size(20.dp),
                contentAlignment = Alignment.Center
            ) {
                avatar()
            }
        }
        if (author != null) {
            ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                author()
            }
        }
        if (content != null) {
            ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                content()
            }
        }
    }
}

@Composable
fun WidgetMessageReplyContent(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
        inlineContent = textInlineContent(),
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
fun WidgetMessageReplyAuthor(
    author: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = author,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
fun WidgetMessageAvatar(
    url: String,
    modifier: Modifier = Modifier,
) {
    OCAsyncImage(
        modifier = modifier
            .clip(CircleShape),
        url = url,
    )
}

@Composable
fun WidgetMessageAuthor(
    author: String,
    timestamp: String,
    edited: Boolean,
    modifier: Modifier = Modifier,
    onAuthorClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProvideTextStyle(MaterialTheme.typography.labelLarge) {
            Text(
                text = author,
                modifier = Modifier
                    .clickable(
                        enabled = onAuthorClick != null,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onAuthorClick?.invoke()
                    }
            )
        }
        ProvideContentAlpha(ContentAlpha.low) {
            Text("·")
            ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                Text(timestamp)
            }
            if (edited) {
                Text("·")
                Icon(
                    modifier = Modifier
                        .size(12.dp),
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun WidgetMessageContent(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
        inlineContent = textInlineContent()
    )
}

@Composable
private fun textInlineContent(): Map<String, InlineTextContent> {
    val emoteSize = (LocalTextStyle.current.fontSize.value + 2f).sp
    return mapOf(
        "emote" to InlineTextContent(
            placeholder = Placeholder(
                width = emoteSize,
                height = emoteSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) { emoteId ->
            OCAsyncImage(
                url = "${BuildConfig.URL_CDN}/emojis/$emoteId",
            )
        }
    )
}