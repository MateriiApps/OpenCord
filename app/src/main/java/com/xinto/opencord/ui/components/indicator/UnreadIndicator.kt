package com.xinto.opencord.ui.components.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UnreadIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topEnd = 12.dp,
                    bottomEnd = 12.dp,
                ),
            )
            .background(Color.White)
            .width(4.dp),
    )
}
