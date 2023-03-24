package com.xinto.opencord.ui.screens.home.panels.messagemenu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.xinto.opencord.ui.components.message.MessageRegular

@Composable
fun MessageMenuLoading() {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .padding(horizontal = 20.dp),
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
        ) {
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
                    Box(
                        modifier = Modifier
                            .shimmer(shimmer)
                            .size(width = remember { (30..100).random().dp }, height = 14.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
                    )
                },
                content = {
                    val lineCount = remember { (1..3).random() }
                    for (i in 0 until lineCount) key(i) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            val blockCount = remember { (1..5).random() }
                            for (j in 0 until blockCount) key(j) {
                                Text(
                                    text = remember { " ".repeat((10..30).random()) },
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

        Divider(
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            repeat(6) {
                Box(
                    modifier = Modifier
                        .shimmer(shimmer)
                        .size(42.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)),
                )
            }
        }

        val itemCount = remember { (4..8).random() }
        for (i in 0 until itemCount) key(i) {
            Surface(
                shape = MaterialTheme.shapes.medium,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 13.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .shimmer(shimmer)
                            .size(25.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                    )

                    Text(
                        text = remember { " ".repeat((10..30).random()) },
                        modifier = Modifier
                            .shimmer(shimmer)
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                    )
                }
            }
        }
    }
}
