package com.xinto.opencord.ui.widgets.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xinto.opencord.ui.component.bottomsheet.BottomSheetDialog
import coil.annotation.ExperimentalCoilApi
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.ui.component.image.rememberOpenCordCachePainter
import com.xinto.opencord.ui.component.text.Text
import com.xinto.opencord.ui.simpleast.render.render
import com.xinto.opencord.util.SimpleAstParser
import org.koin.androidx.compose.get

@OptIn(
    ExperimentalCoilApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun WidgetChatMessage(
    message: DomainMessage,
    modifier: Modifier = Modifier,
) {
    val parser = get<SimpleAstParser>()

    val userImage = rememberOpenCordCachePainter(message.author.avatarUrl)

    var showBottomDialog by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .combinedClickable(
                onLongClick = {
                    showBottomDialog = true
                },
                onClick = {}
            )
            .then(modifier),
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
            Text(
                text = parser.render(
                    source = message.content,
                    initialState = null,
                    renderContext = null
                ).toAnnotatedString(),
                style = MaterialTheme.typography.body2,
                inlineContent = mapOf(
                    "emote" to InlineTextContent(
                        placeholder = Placeholder(
                            width = 20.sp,
                            height = 20.sp,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                        )
                    ) { emoteId ->
                        val image = rememberOpenCordCachePainter("${BuildConfig.URL_CDN}/emojis/$emoteId")
                        Image(
                            painter = image,
                            contentDescription = "Emote"
                        )
                    }
                )
            )
        }
    }

    if (showBottomDialog) {
        BottomSheetDialog(
            onDismissRequest = {
                showBottomDialog = false
            }
        ) {
        }
    }
}