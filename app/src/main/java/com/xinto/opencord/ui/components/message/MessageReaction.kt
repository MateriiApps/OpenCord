package com.xinto.opencord.ui.components.message

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = count.toString(),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(start = 3.dp),
                )
            }
        },
        leadingIcon = {
            Box(modifier = Modifier.padding(start = 3.dp)) {
                emote()
            }
        },
    )
}
