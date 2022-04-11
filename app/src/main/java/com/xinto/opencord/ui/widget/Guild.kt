package com.xinto.opencord.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun WidgetGuildListItem(
    onClick: () -> Unit,
    selected: Boolean,
    showIndicator: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val indicatorFraction by animateFloatAsState(if (selected) 0.8f else 0.15f)
    val imageCornerRadius by animateIntAsState(if (selected) 25 else 50)
    Box(
        modifier = modifier.height(48.dp),
    ) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.CenterStart),
            visible = showIndicator || selected,
            enter = slideInHorizontally(),
            exit = slideOutHorizontally()
        ) {
            UnreadIndicator(
                modifier = Modifier.fillMaxHeight(indicatorFraction)
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 12.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(imageCornerRadius))
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.primary,
                LocalTextStyle provides MaterialTheme.typography.headlineSmall
            ) {
                content()
            }
        }
    }
}