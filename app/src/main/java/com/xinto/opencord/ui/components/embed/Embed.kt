package com.xinto.opencord.ui.components.embed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    fields: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
) {
    val stripeColor = (color ?: LocalContentColor.current)
        .compositeOver(MaterialTheme.colorScheme.surface)

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp,
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(4.dp)
                    .background(stripeColor),
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
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
                if (footer != null) {
                    footer()
                }
            }
        }
    }
}
