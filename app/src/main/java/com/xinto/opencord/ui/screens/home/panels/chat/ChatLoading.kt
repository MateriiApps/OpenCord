package com.xinto.opencord.ui.screens.home.panels.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.xinto.opencord.ui.components.message.MessageRegular

@Composable
fun ChatLoading(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .verticalScroll(
                state = rememberScrollState(),
                enabled = false,
            ),
    )
    {
        repeat(10) {
            MessageRegular(
                modifier = Modifier.fillMaxWidth(),
                avatar = {
                    Box(
                        modifier = Modifier
                            .shimmer(shimmer)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)),
                    )
                },
                author = {
                    val width = remember { (30..100).random().dp }
                    Box(
                        modifier = Modifier
                            .shimmer(shimmer)
                            .size(width = width, height = 14.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
                    )
                },
                content = {
                    val rowCount = remember { (1..3).random() }
                    repeat(rowCount) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            val itemCount = remember { (1..5).random() }
                            repeat(itemCount) {
                                val spaces = remember { (10..30).random() }
                                Text(
                                    text = " ".repeat(spaces),
                                    modifier = Modifier
                                        .shimmer(shimmer)
                                        .padding(top = 8.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)),
                                )
                            }
                        }
                    }
                },
            )
        }
    }
}
