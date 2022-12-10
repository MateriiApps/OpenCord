package com.xinto.opencord.ui.components.message.reply

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MessageReferenced(
    modifier: Modifier = Modifier,
    avatar: @Composable (() -> Unit)? = null,
    author: @Composable (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (avatar != null) {
            Box(
                modifier = Modifier.size(20.dp),
                contentAlignment = Alignment.Center,
            ) {
                avatar()
            }
        }
        if (author != null) {
            ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                author()
            }
        }
        if (content != null) {
            ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                content()
            }
        }
    }
}
