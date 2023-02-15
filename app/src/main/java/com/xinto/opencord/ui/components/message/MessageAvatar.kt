package com.xinto.opencord.ui.components.message

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.size.Size
import com.xinto.opencord.ui.components.OCAsyncImage

@Composable
fun MessageAvatar(
    url: String,
    modifier: Modifier = Modifier,
) {
    OCAsyncImage(
        url = url,
        size = Size(100, 100),
        modifier = modifier
            .clip(CircleShape),
    )
}
