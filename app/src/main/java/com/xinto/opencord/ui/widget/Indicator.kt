package com.xinto.opencord.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun UnreadIndicator(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStartPercent = 0,
                    topEndPercent = 100,
                    bottomStartPercent = 0,
                    bottomEndPercent = 100
                )
            )
            .background(LocalContentColor.current)
            .width(4.dp)
    )
}