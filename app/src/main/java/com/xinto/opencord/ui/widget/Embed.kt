package com.xinto.opencord.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp

@Composable
fun WidgetEmbed(
    title: String?,
    description: String?,
    color: Color?,
    modifier: Modifier = Modifier,
    author: (@Composable () -> Unit)? = null,
    fields: (@Composable () -> Unit)? = null,
    footer: (@Composable () -> Unit)? = null,
) {
    val stripeColor = (color ?: LocalContentColor.current).compositeOver(MaterialTheme.colorScheme.surface)
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp)
                    .background(stripeColor)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (author != null) {
                    author()
                }
                if (title != null) {
                    ProvideTextStyle(MaterialTheme.typography.titleSmall) {
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
                        verticalArrangement = Arrangement.spacedBy(6.dp)
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

@Composable
fun WidgetEmbedAuthor(
    name: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        ProvideTextStyle(MaterialTheme.typography.labelMedium) {
            Text(name)
        }
    }
}

@Composable
fun WidgetEmbedField(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ProvideTextStyle(MaterialTheme.typography.titleSmall) {
            Text(name)
        }
        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
            Text(value)
        }
    }
}

@Composable
fun WidgetEmbedFooter(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProvideTextStyle(MaterialTheme.typography.labelSmall) {
            Text(text)
        }
    }
}