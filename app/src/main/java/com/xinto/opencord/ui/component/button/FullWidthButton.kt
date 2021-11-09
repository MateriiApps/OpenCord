package com.xinto.opencord.ui.component.button

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun FullWidthButton(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    content: @Composable RowScope.() -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.defaultMinSize(24.dp, 24.dp)) {
            icon()
        }
        content()
    }
}