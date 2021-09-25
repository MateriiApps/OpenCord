package com.xinto.opencord.ui.widgets.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.network.util.discordCdnUrl
import com.xinto.opencord.ui.component.image.rememberOpenCordCachePainter
import com.xinto.opencord.ui.component.text.OpenCordText
import com.xinto.opencord.ui.simpleast.render.render
import com.xinto.opencord.util.SimpleAstParser
import org.koin.androidx.compose.get

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterialApi::class)
@Composable
fun WidgetChatMessage(
    message: DomainMessage,
    modifier: Modifier = Modifier,
) {
    val parser = get<SimpleAstParser>()

    val userImage = rememberOpenCordCachePainter(message.author.avatarUrl)
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
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            OpenCordText(
                text = message.author.username,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            OpenCordText(
                text = parser.render(
                    source = message.content,
                    initialState = null,
                    renderContext = null
                ).toAnnotatedString(),
                style = MaterialTheme.typography.body2,
                inlineContent = mapOf(
                    "emote" to InlineTextContent(
                        placeholder = Placeholder(
                            width = 24.sp,
                            height = 24.sp,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                        )
                    ) { emoteId ->
                        val image = rememberOpenCordCachePainter("$discordCdnUrl/emojis/$emoteId")
                        Image(
                            painter = image,
                            contentDescription = "Emote"
                        )
                    }
                )
            )
        }
    }
}