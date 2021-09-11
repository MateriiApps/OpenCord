package com.xinto.opencord.ui.component.layout

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OpenCordBackground(
    modifier: Modifier = Modifier,
    backgroundColorAlpha: Float = 1f,
    content: @Composable () -> Unit,
) {
    val backgroundColor = MaterialTheme.colors.background
    Surface(
        modifier = modifier,
        color = backgroundColor.copy(alpha = backgroundColorAlpha),
        contentColor = contentColorFor(backgroundColor),
        content = content,
    )
}