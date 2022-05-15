package com.xinto.opencord.ui.widget

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun WidgetBranchBTR(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outline
) {
    Canvas(modifier = modifier) {
        drawPath(
            path = Path().apply {
                moveTo(
                    x = size.width / 2,
                    y = size.height
                )
                cubicTo(
                    x1 = size.width / 2.1f,
                    y1 = size.height / 2.1f,
                    x2 = size.width / 1.9f,
                    y2 = size.height / 1.9f,
                    x3 = size.width,
                    y3 = size.height / 2
                )
            },
            color = color,
            style = Stroke(width = 5f)
        )
    }
}