package com.xinto.opencord.ui.components.guild.list

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GuildsListTextItem(
    iconText: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        tonalElevation = 1.dp,
    ) {
        BoxWithConstraints(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(48.dp),
        ) {
            val baseTextSize = LocalDensity.current.run { maxWidth.toSp() } * 0.9f
            var scaledTextSize by remember(iconText) { mutableStateOf(baseTextSize) }
            var hasOverflowed by remember(iconText) { mutableStateOf(false) }
            var shouldDisplay by remember { mutableStateOf(false) }

            Text(
                text = iconText,
                style = MaterialTheme.typography.headlineSmall,
                color = LocalContentColor.current,
                fontSize = scaledTextSize,
                letterSpacing = scaledTextSize * if (hasOverflowed) 0.15f else 0f,
                lineHeight = scaledTextSize * if (hasOverflowed) 1.15f else 1f,
                maxLines = if (hasOverflowed) 2 else 1,
                softWrap = true,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(if (hasOverflowed) 6.dp else 4.dp)
                    .alpha(if (shouldDisplay) 1f else 0f),
                onTextLayout = { layout ->
                    if (layout.hasVisualOverflow) {
                        scaledTextSize *= 0.9f
                    } else if (layout.lineCount > 1) {
                        hasOverflowed = true
                    } else {
                        shouldDisplay = true
                    }
                },
            )
        }
    }
}
