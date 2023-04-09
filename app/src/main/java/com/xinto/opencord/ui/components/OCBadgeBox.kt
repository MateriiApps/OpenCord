package com.xinto.opencord.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun OCBadgeBox(
    modifier: Modifier = Modifier,
    badge: (@Composable () -> Unit)?,
    badgeShape: Shape = CircleShape,
    badgeAlignment: Alignment = Alignment.BottomEnd,
    badgePadding: PaddingValues = PaddingValues(),
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        content()
        if (badge != null) {
            Surface(
                shape = badgeShape,
                modifier = Modifier
                    .padding(badgePadding)
                    .align(badgeAlignment),
            ) {
                Box(
                    modifier = Modifier.padding(3.dp),
                ) {
                    badge()
                }
            }
        }
    }
}
