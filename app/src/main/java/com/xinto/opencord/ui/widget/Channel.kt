package com.xinto.opencord.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WidgetChannelListItem(
    onClick: () -> Unit,
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    selected: Boolean,
    showIndicator: Boolean,
    modifier: Modifier = Modifier,
) {
    val indicatorFraction by animateFloatAsState(if (selected) 0.7f else 0.15f)
    val tonalElevation by animateDpAsState(if (selected) 5.dp else 0.dp)
    Box(
        modifier = modifier.height(36.dp),
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
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            shape = MaterialTheme.shapes.medium,
            onClick = onClick,
            tonalElevation = tonalElevation
        ) {
            Row(
                modifier = Modifier.padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
                ProvideTextStyle(MaterialTheme.typography.titleMedium) {
                    title()
                }
            }
        }
    }
}