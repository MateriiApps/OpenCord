package com.xinto.opencord.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.model.DomainAttachment
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.simpleast.render.render
import com.xinto.opencord.ui.component.OCBasicTextField
import com.xinto.opencord.ui.component.rememberOCCoilPainter
import com.xinto.opencord.util.SimpleAstParser

@Composable
fun WidgetChatMessage(
    message: DomainMessage,
    parser: SimpleAstParser,
    modifier: Modifier = Modifier,
) {
    val userImage = rememberOCCoilPainter(message.author.avatarUrl)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            painter = userImage,
            contentDescription = null
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 40.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                text = message.author.username,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            if (message.content.isNotEmpty()) {
                WidgetMessageContent(
                    text = parser.render(
                        source = message.content,
                        initialState = null,
                        renderContext = null
                    ).toAnnotatedString()
                )
            }
            WidgetMessageAttachments(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .padding(vertical = 4.dp),
                attachments = message.attachments
            )
        }
    }
}

@Composable
fun WidgetChatInput(
    value: String,
    onValueChange: (value: String) -> Unit,
    onSendClick: () -> Unit,
) {
    val shapePercent = when (value.lines().size) {
        0 -> 50
        1 -> 50
        2 -> 40
        3 -> 30
        else -> 25
    }
    Row(
        modifier = Modifier
            .padding(8.dp)
            .imePadding(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OCBasicTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(shapePercent),
            maxLines = 4
        )
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterVertically),
            visible = value.isNotBlank(),
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
                    .clickable(onClick = onSendClick)
                    .size(36.dp),
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "Send"
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
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.body2,
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