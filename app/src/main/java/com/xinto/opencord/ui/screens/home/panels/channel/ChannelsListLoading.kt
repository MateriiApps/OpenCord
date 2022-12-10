package com.xinto.opencord.ui.screens.home.panels.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.xinto.opencord.ui.components.channel.list.ChannelListRegularItem
import com.xinto.opencord.ui.components.channel.list.ChannelListCategoryItem

@Composable
fun ChannelsListLoading(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    Column(modifier = modifier) {
        val items = remember { (5..20).random() }
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
        ) {
            val guildName = remember { " ".repeat((20..30).random()) }
            val shimmerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .shimmer(shimmer)
                    .clip(CircleShape)
                    .background(shimmerColor),
            )
            Text(
                modifier = Modifier
                    .shimmer(shimmer)
                    .clip(MaterialTheme.shapes.medium)
                    .background(shimmerColor),
                text = guildName,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        repeat(items) { itemIndex ->
            val isCategory = remember { itemIndex == 0 || (0..6).random() == 1 }
            if (isCategory) {
                ChannelListCategoryItem(
                    modifier = Modifier.padding(
                        top = 12.dp,
                        bottom = 4.dp,
                    ),
                    title = {
                        val title = remember { " ".repeat((10..20).random()) }
                        Text(
                            modifier = Modifier
                                .shimmer(shimmer)
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                            text = title,
                        )
                    },
                    icon = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmer(shimmer)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                        )
                    },
                    onClick = {},
                )
            } else {
                ChannelListRegularItem(
                    modifier = Modifier.padding(bottom = 2.dp),
                    title = {
                        val title = remember { " ".repeat((15..30).random()) }
                        Text(
                            modifier = Modifier
                                .shimmer(shimmer)
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)),
                            text = title,
                        )
                    },
                    icon = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .shimmer(shimmer)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)),
                        )
                    },
                    selected = false,
                    showIndicator = false,
                    onClick = {},
                )
            }
        }
    }
}
