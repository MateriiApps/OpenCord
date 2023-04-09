package com.xinto.opencord.ui.components.embed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun Embed(
    title: String?,
    url: String?,
    description: String?,
    color: Color?,
    modifier: Modifier = Modifier,
    author: (@Composable () -> Unit)? = null,
    media: (@Composable () -> Unit)? = null,
    thumbnail: (@Composable () -> Unit)? = null,
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
            Row {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp),
                ) {
                    if (author != null) {
                        author()
                    }

                    if (title != null) {
                        if (url == null) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge,
                                overflow = TextOverflow.Ellipsis,
                            )
                        } else {
                            val uriHandler = LocalUriHandler.current
                            ClickableText(
                                text = buildAnnotatedString { append(title) },
                                style = MaterialTheme.typography.labelLarge.copy(
                                    textDecoration = TextDecoration.Underline,
                                    color = MaterialTheme.colorScheme.primary,
                                ),
                                overflow = TextOverflow.Ellipsis,
                                onClick = {
                                    uriHandler.openUri(url)
                                },
                            )
                        }
                    }

                    if (description != null) {
                        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                            Text(description)
                        }
                    }
                }

                if (thumbnail != null) {
                    thumbnail()
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

            if (media != null) {
                media()
            }

            if (footer != null) {
                footer()
            }
        }
    }
}
