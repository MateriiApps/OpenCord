package com.xinto.opencord.ui.components.message

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.xinto.opencord.ui.components.OCAsyncImage

@Composable
fun MessageAvatar(
    url: String,
    modifier: Modifier = Modifier,
) {
    OCAsyncImage(
        modifier = modifier
            .clip(CircleShape),
        url = url,
    )
}
