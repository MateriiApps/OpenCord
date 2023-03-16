package com.xinto.opencord.ui.components.message

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.xinto.opencord.ui.components.OCImage
import com.xinto.opencord.ui.components.OCSize

@Composable
fun MessageAvatar(
    url: String,
    modifier: Modifier = Modifier,
) {
    OCImage(
        url = url,
        size = OCSize(100, 100),
        modifier = modifier
            .clip(CircleShape),
    )
}
