package com.xinto.opencord.ui.widgets.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.ui.component.text.OpenCordText

@OptIn(ExperimentalCoilApi::class)
@Composable
fun WidgetChatMessage(
    message: DomainMessage,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val request = ImageRequest.Builder(context)
        .data(message.author.avatarUrl)
        .diskCachePolicy(CachePolicy.ENABLED)
        .build()
    val userImage = rememberImagePainter(request)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
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
                text = message.content,
                style = MaterialTheme.typography.body2
            )
        }
    }
}