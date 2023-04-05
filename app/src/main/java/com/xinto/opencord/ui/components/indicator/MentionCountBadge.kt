package com.xinto.opencord.ui.components.indicator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun MentionCountBadge(
    mentionCount: Int,
    modifier: Modifier = Modifier,
) {
    val countDisplay by remember(mentionCount) {
        derivedStateOf {
            when {
                mentionCount < 999 -> mentionCount.toString()
                mentionCount > 10000 -> "10k+"
                mentionCount.mod(1000) < 100 -> "${mentionCount / 1000}k"
                else -> "%.1fk".format(mentionCount / 1000f)
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(percent = 100))
            .defaultMinSize(18.dp)
            .background(MaterialTheme.colorScheme.onPrimaryContainer),
    ) {
        AnimatedContent(
            targetState = countDisplay,
            transitionSpec = {
                val direction = if (targetState > initialState) {
                    AnimatedContentScope.SlideDirection.Up
                } else {
                    AnimatedContentScope.SlideDirection.Down
                }

                slideIntoContainer(direction) with slideOutOfContainer(direction)
            },
            modifier = Modifier
                .padding(horizontal = 3.dp),
        ) {
            Text(
                text = countDisplay,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Visible,
            )
        }
    }
}
