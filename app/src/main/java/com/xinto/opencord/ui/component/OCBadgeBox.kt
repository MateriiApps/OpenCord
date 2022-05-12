package com.xinto.opencord.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OCBadgeBox(
    badge: (@Composable () -> Unit)?,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        content()
        if (badge != null) {
            Surface(
                modifier = Modifier.align(Alignment.BottomEnd),
                shape = CircleShape,
            ) {
                Box(modifier = Modifier.padding(3.dp)) {
                    badge()
                }
            }
        }
    }
}