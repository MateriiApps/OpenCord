package com.xinto.opencord.ui.components.message

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Surface(
        shape = MaterialTheme.shapes.small,
        border = if (meReacted) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else null,
        color = if (meReacted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier
            .padding(bottom = 5.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(vertical = 5.dp, horizontal = 9.dp),
        ) {
            emote()

            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    val direction = if (targetState > initialState) {
                        AnimatedContentScope.SlideDirection.Up
                    } else {
                        AnimatedContentScope.SlideDirection.Down
                    }

                    slideIntoContainer(direction) with slideOutOfContainer(direction)
                },
            ) { count ->
                Text(
                    text = count.toString(),
                    fontSize = 16.sp,
                )
            }
        }
    }
}
