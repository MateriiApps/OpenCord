package com.xinto.opencord.ui.components.embed

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp

@Composable
fun Embed(
    title: String?,
    description: String?,
    color: Color?,
    modifier: Modifier = Modifier,
    author: (@Composable () -> Unit)? = null,
    image: (@Composable () -> Unit)? = null,
    fields: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp,
    ) {
        val surfaceColor = MaterialTheme.colorScheme.surface

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    if (color != null) {
                        drawRect(
                            color.compositeOver(surfaceColor),
                            topLeft = Offset.Zero,
                            size = Size(4.dp.toPx(), size.height),
                        )
                    }
                }
                .padding(start = 16.dp, top = 14.dp, end = 14.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (author != null) {
                author()
            }
            if (title != null) {
                ProvideTextStyle(MaterialTheme.typography.labelLarge) {
                    Text(title)
                }
            }
            if (description != null) {
                ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                    Text(description)
                }
            }
            if (fields != null) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    fields()
                }
            }
            if (image != null) {
                image()
            }
            if (footer != null) {
                footer()
            }
        }
    }
}
