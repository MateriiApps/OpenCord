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
import androidx.compose.runtime.getValue
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
import com.xinto.opencord.domain.channel.DomainCategoryChannel
import com.xinto.opencord.domain.channel.DomainChannel
import com.xinto.opencord.domain.channel.DomainTextChannel
import com.xinto.opencord.domain.channel.DomainVoiceChannel
import com.xinto.opencord.ui.components.OCAsyncImage
import com.xinto.opencord.ui.components.channel.list.ChannelListRegularItem
import com.xinto.opencord.ui.components.channel.list.ChannelListCategoryItem
import com.xinto.opencord.ui.util.ContentAlpha
import com.xinto.opencord.ui.util.ProvideContentAlpha
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

@Composable
fun ChannelsListLoaded(
    onChannelSelect: (Long) -> Unit,
    onCategoryClick: (Long) -> Unit,
    selectedChannelId: Long,
    bannerUrl: String?,
    boostLevel: Int,
    guildName: String,
    channels: ImmutableMap<DomainCategoryChannel?, ImmutableList<DomainChannel>>,
    collapsedCategories: ImmutableList<Long>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
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
        for ((category, categoryChannels) in channels) {
            //TODO put this in remember
            val collapsed = collapsedCategories.contains(category?.id)

            if (category != null) {
                item {
                    ProvideContentAlpha(ContentAlpha.medium) {
                        val iconRotation by animateFloatAsState(
                            targetValue = if (collapsed) -90f else 0f,
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
                            title = { Text(category.name.uppercase()) },
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_keyboard_arrow_down),
                                    contentDescription = stringResource(R.string.channels_collapse_category),
                                    modifier = Modifier.rotate(iconRotation),
                                )
                            },
                            onClick = {
                                onCategoryClick(category.id)
                            },
                        )
                    }
                }
            }

            items(categoryChannels) { channel ->
//                if (channel.canView) {
                if (!(channel.id != selectedChannelId && collapsed)) {
                    when (channel) {
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
                                selected = selectedChannelId == channel.id,
                                showIndicator = selectedChannelId != channel.id,
                                onClick = {
                                    onChannelSelect(channel.id)
                                },
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
                                showIndicator = false,
                                onClick = { /*TODO*/ },
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}
