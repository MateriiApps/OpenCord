package com.xinto.opencord.ui.widget

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R

@Composable
fun WidgetCategory(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit),
    icon: (@Composable () -> Unit),
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.medium,
                LocalTextStyle provides MaterialTheme.typography.labelMedium
            ) {
                Box(
                    modifier = Modifier.size(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    icon()
                }
                title()
            }
        }
    }

}