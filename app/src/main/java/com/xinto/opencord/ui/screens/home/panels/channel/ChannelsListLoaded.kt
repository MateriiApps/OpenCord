package com.xinto.opencord.ui.screens.home.panels.channel

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.channel.DomainTextChannel
import com.xinto.opencord.domain.channel.DomainVoiceChannel
import com.xinto.opencord.ui.components.OCAsyncImage
import com.xinto.opencord.ui.components.channel.list.ChannelListCategoryItem
import com.xinto.opencord.ui.components.channel.list.ChannelListRegularItem
import com.xinto.opencord.ui.util.ContentAlpha
import com.xinto.opencord.ui.util.ProvideContentAlpha
import com.xinto.opencord.ui.viewmodel.ChannelsViewModel

@Composable
fun ChannelsListLoaded(
    onChannelSelect: (Long) -> Unit,
    onCategoryClick: (Long) -> Unit,
    selectedChannelId: Long,
    bannerUrl: String?,
    boostLevel: Int,
    guildName: String,
    categoryChannels: SnapshotStateMap<Long, ChannelsViewModel.CategoryItemData>,
    noCategoryChannels: SnapshotStateMap<Long, ChannelsViewModel.ChannelItemData>,
    modifier: Modifier = Modifier
) {
    val sortedCategoryChannels by remember(categoryChannels) {
        derivedStateOf {
            categoryChannels.values.sortedBy { it.channel.position }
        }
    }
    val sortedNoCategoryChannels by remember(noCategoryChannels) {
        derivedStateOf {
            noCategoryChannels.values.sortedBy { it.channel.position }
        }
    }

    LazyColumn(
        modifier = modifier,
    ) {
        item(key = "CHANNEL_HEADER") {
            Box(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(IntrinsicSize.Min),
            ) {
                if (bannerUrl != null) {
                    OCAsyncImage(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .height(150.dp),
                        url = bannerUrl,
                        contentScale = ContentScale.Crop,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillParentMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.DarkGray,
                                        Color.Transparent,
                                    ),
                                ),
                                alpha = 0.8f,
                            ),
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .padding(14.dp),
                ) {
                    val boostIcon = when (boostLevel to (bannerUrl != null)) {
                        1 to false -> R.drawable.ic_guild_badge_premium_tier_1
                        1 to true -> R.drawable.ic_guild_badge_premium_tier_1_banner
                        2 to false -> R.drawable.ic_guild_badge_premium_tier_2
                        2 to true -> R.drawable.ic_guild_badge_premium_tier_2_banner
                        3 to false -> R.drawable.ic_guild_badge_premium_tier_3
                        3 to true -> R.drawable.ic_guild_badge_premium_tier_3_banner
                        else -> null
                    }
                    if (boostIcon != null) {
                        Icon(
                            painter = painterResource(id = boostIcon),
                            contentDescription = stringResource(R.string.guild_boost_level),
                            modifier = Modifier.size(20.dp),
                            tint = Color.Unspecified,
                        )
                    }
                    Text(
                        text = guildName,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge.copy(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(0f, 5f),
                                blurRadius = 3f,
                            ),
                        ),
                    )
                }
            }
        }

        items(sortedNoCategoryChannels, key = { it.channel.id }) { itemChannel ->
            ChannelItem(
                itemData = itemChannel,
                isSelected = selectedChannelId == itemChannel.channel.id,
                onClick = { onChannelSelect(itemChannel.channel.id) },
            )
        }

        for (categoryItem in sortedCategoryChannels) {
            item(key = categoryItem.channel.id) {
                ProvideContentAlpha(ContentAlpha.medium) {
                    val iconRotation by animateFloatAsState(
                        targetValue = if (categoryItem.collapsed) -90f else 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow,
                        ),
                    )
                    ChannelListCategoryItem(
                        modifier = Modifier.padding(
                            top = 12.dp,
                            bottom = 4.dp,
                        ),
                        title = {
                            Text(categoryItem.channel.name.uppercase())
                        },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_keyboard_arrow_down),
                                contentDescription = stringResource(R.string.channels_collapse_category),
                                modifier = Modifier.rotate(iconRotation),
                            )
                        },
                        onClick = {
                            onCategoryClick(categoryItem.channel.id)
                        },
                    )
                }
            }

            if (!categoryItem.collapsed) {
                items(
                    items = categoryItem.subChannels.values.sortedBy { it.channel.position },
                    key = { it.channel.id },
                ) { itemChannel ->
                    ChannelItem(
                        itemData = itemChannel,
                        isSelected = selectedChannelId == itemChannel.channel.id,
                        onClick = { onChannelSelect(itemChannel.channel.id) },
                    )
                }
            }
        }
    }
}

@Composable
fun ChannelItem(
    itemData: ChannelsViewModel.ChannelItemData,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    when (val channel = itemData.channel) {
        is DomainTextChannel -> {
            ChannelListRegularItem(
                modifier = Modifier.padding(bottom = 2.dp),
                title = { Text(channel.name) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_tag),
                        contentDescription = null,
                    )
                },
                selected = isSelected,
                showUnread = itemData.isUnread,
                onClick = onClick,
            )
        }
        is DomainVoiceChannel -> {
            ChannelListRegularItem(
                modifier = Modifier.padding(bottom = 2.dp),
                title = { Text(channel.name) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_volume_up),
                        contentDescription = null,
                    )
                },
                selected = false,
                showUnread = false,
                onClick = { /*TODO*/ },
            )
        }
        else -> Unit
    }
}
