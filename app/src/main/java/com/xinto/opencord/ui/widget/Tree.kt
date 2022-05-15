package com.xinto.opencord.ui.widget

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun WidgetBranchReply(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outline
) {
    Canvas(modifier = modifier) {
        drawPath(
            path = Path().apply {
                moveTo(
                    x = 0f,
                    y = size.height
                )
                cubicTo(
                    x1 = 0f,
                    y1 = 0f,
                    x2 = 0f,
                    y2 = 0f,
                    x3 = size.width,
                    y3 = 0f
                )
            },
            color = color,
            style = Stroke(
                width = 5f,
            )
        )
    }
}