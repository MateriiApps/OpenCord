package com.xinto.opencord.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.R
import com.xinto.opencord.ui.component.OCAsyncImage

@Composable
fun WidgetChatMessage(
    modifier: Modifier = Modifier,
    avatar: (@Composable () -> Unit)? = null,
    author: (@Composable () -> Unit)? = null,
    content: (@Composable () -> Unit)? = null,
    attachments: (@Composable () -> Unit)? = null,
    embeds: (@Composable () -> Unit)? = null,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (avatar != null) {
                avatar()
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
                        content()
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

@Composable
fun WidgetMessageAvatar(
    url: String,
    modifier: Modifier = Modifier,
) {
    OCAsyncImage(
        modifier = modifier
            .size(40.dp)
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
                author,
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
        CompositionLocalProvider(
            LocalContentColor provides LocalContentColor.current.copy(alpha = 0.7f)
        ) {
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
                    OCAsyncImage(
                        url = "${BuildConfig.URL_CDN}/emojis/$emoteId",
                    )
                }
            )
        )
    }
}