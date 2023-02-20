package com.xinto.opencord.ui.components.message

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MessageReaction(
    onClick: () -> Unit,
    count: Int,
    meReacted: Boolean,
    modifier: Modifier = Modifier,
    emote: @Composable () -> Unit,
) {
    FilterChip(
        selected = meReacted,
        modifier = modifier,
        onClick = onClick,
        label = {
            Text(count.toString())
        },
        leadingIcon = emote,
    )
}
