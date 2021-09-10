package com.xinto.opencord.ui.component.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun UnreadIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStartPercent = 0,
                    bottomStartPercent = 0,
                    topEndPercent = 100,
                    bottomEndPercent = 100))
            .background(MaterialTheme.colors.onBackground)
            .width(4.dp))
}