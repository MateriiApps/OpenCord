package com.xinto.opencord.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.ui.component.OCBadgeBox
import com.xinto.opencord.ui.component.rememberOCCoilPainter
import com.xinto.opencord.ui.viewmodel.ChannelsViewModel
import com.xinto.opencord.ui.viewmodel.CurrentUserViewModel
import com.xinto.opencord.ui.viewmodel.GuildsViewModel
import com.xinto.opencord.ui.widget.*
import com.xinto.opencord.util.letComposable
import org.koin.androidx.compose.getViewModel

@Composable
fun GuildsChannelsScreen(
    onSettingsClick: () -> Unit,
    onGuildSelect: () -> Unit,
    onChannelSelect: () -> Unit,
    modifier: Modifier = Modifier,
    guildsViewModel: GuildsViewModel = getViewModel(),
    channelsViewModel: ChannelsViewModel = getViewModel(),
    currentUserViewModel: CurrentUserViewModel = getViewModel()
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.weight(1f),
        ) {
            GuildsList(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                onGuildSelect = onGuildSelect,
                viewModel = guildsViewModel
            )
            ChannelsList(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(3.5f),
                onChannelSelect = onChannelSelect,
                viewModel = channelsViewModel
            )
        }
        CurrentUserItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp),
            viewModel = currentUserViewModel,
            onSettingsClick = onSettingsClick
        )
    }
}

@Composable
private fun GuildsList(
    onGuildSelect: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuildsViewModel = getViewModel()
) {
    when (viewModel.state) {
        GuildsViewModel.State.Loading -> {
            GuildsListLoading(modifier = modifier)
        }
        GuildsViewModel.State.Loaded -> {
            GuildsListLoaded(
                modifier = modifier,
                onGuildSelect = {
                    viewModel.selectGuild(it)
                    onGuildSelect()
                },
                guilds = viewModel.guilds.values.toList(),
                selectedGuildId = viewModel.selectedGuildId
            )
        }
        GuildsViewModel.State.Error -> {

        }
    }
}

@Composable
private fun ChannelsList(
    onChannelSelect: () -> Unit,
    viewModel: ChannelsViewModel,
    modifier: Modifier = Modifier
) {
    val sortedChannels by remember(viewModel.channels) {
        derivedStateOf {
            viewModel.getSortedChannels()
        }
    }
    CompositionLocalProvider(LocalAbsoluteTonalElevation provides 1.dp) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.large,
        ) {
            when (viewModel.state) {
                is ChannelsViewModel.State.Unselected -> {
                    ChannelsListUnselected(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is ChannelsViewModel.State.Loading -> {
                    ChannelsListLoading(
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is ChannelsViewModel.State.Loaded -> {
                    ChannelsListLoaded(
                        modifier = Modifier.fillMaxSize(),
                        onChannelSelect = {
                            viewModel.selectChannel(it)
                            onChannelSelect()
                        },
                        onCategoryClick = {
                            viewModel.toggleCategory(it)
                        },
                        bannerUrl = viewModel.guildBannerUrl,
                        boostLevel = viewModel.guildBoostLevel,
                        guildName = viewModel.guildName,
                        channels = sortedChannels,
                        collapsedCategories = viewModel.collapsedCategories,
                        selectedChannelId = viewModel.selectedChannelId
                    )
                }
                is ChannelsViewModel.State.Error -> {

                }
            }
        }
    }
}

@Composable
private fun CurrentUserItem(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CurrentUserViewModel = getViewModel()
) {
    Surface(
        modifier = modifier,
        onClick = { /*TODO*/ },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    top = 12.dp,
                    bottom = 12.dp,
                    end = 4.dp
                )
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val userIcon = rememberOCCoilPainter(viewModel.avatarUrl)
            OCBadgeBox(
                badge = viewModel.userStatus?.letComposable { userStatus ->
                    WidgetStatusIcon(
                        modifier = Modifier.size(10.dp),
                        userStatus = userStatus
                    )
                }
            ) {
                Image(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    painter = userIcon,
                    contentDescription = null
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                val customStatus = viewModel.userCustomStatus
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ProvideTextStyle(MaterialTheme.typography.titleSmall) {
                        Text(viewModel.username)
                    }
                    if (customStatus != null) {
                        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                            Text(viewModel.discriminator)
                        }
                    }
                }
                ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                    if (customStatus != null) {
                        Text(customStatus)
                    } else {
                        Text(viewModel.discriminator)
                    }
                }
            }
            Row(
                modifier = modifier.weight(1f),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun GuildsListLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun GuildsListLoaded(
    onGuildSelect: (ULong) -> Unit,
    selectedGuildId: ULong,
    guilds: List<DomainGuild>,
    modifier: Modifier = Modifier
) {
    val discordIcon = painterResource(R.drawable.ic_discord_logo)
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            WidgetGuildListItem(
                modifier = Modifier.fillParentMaxWidth(),
                selected = false,
                showIndicator = false,
                onClick = {}
            ) {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .align(Alignment.Center),
                    painter = discordIcon,
                    contentDescription = "Home",
                )
            }
        }

        item {
            Divider(
                modifier = Modifier
                    .fillParentMaxWidth(0.5f)
                    .padding(bottom = 6.dp)
                    .clip(MaterialTheme.shapes.medium),
                thickness = 2.dp
            )
        }

        items(guilds) { guild ->
            WidgetGuildListItem(
                selected = selectedGuildId == guild.id,
                showIndicator = true,
                onClick = {
                    onGuildSelect(guild.id)
                }
            ) {
                if (guild.iconUrl != null) {
                    WidgetGuildContentImage(
                        url = guild.iconUrl
                    )
                } else {
                    WidgetGuildContentVector {
                        Text(guild.iconText)
                    }
                }
            }
        }
    }
}

@Composable
private fun ChannelsListUnselected(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        ProvideTextStyle(MaterialTheme.typography.titleMedium) {
            Text(stringResource(R.string.channel_unselected_message))
        }
    }
}

@Composable
private fun ChannelsListLoading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ChannelsListLoaded(
    onChannelSelect: (ULong) -> Unit,
    onCategoryClick: (ULong) -> Unit,
    selectedChannelId: ULong,
    bannerUrl: String?,
    boostLevel: Int,
    guildName: String,
    channels: Map<DomainChannel.Category?, List<DomainChannel>>,
    collapsedCategories: List<ULong>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                if (bannerUrl != null) {
                    val painter = rememberOCCoilPainter(bannerUrl)
                    Image(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .clip(MaterialTheme.shapes.large)
                            .height(150.dp),
                        painter = painter,
                        contentScale = ContentScale.Crop,
                        contentDescription = "Guild Banner"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillParentMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.DarkGray,
                                        Color.Transparent
                                    ),
                                ),
                                alpha = 0.8f
                            )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopStart)
                        .padding(14.dp)
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
                            contentDescription = "Server boost level icon",
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
                            )
                        ),
                    )
                }
            }
        }
        for ((category, categoryChannels) in channels) {
            val collapsed = collapsedCategories.contains(category?.id)

            if (category != null) item {
                WidgetCategory(
                    modifier = Modifier.padding(
                        top = 12.dp,
                        bottom = 4.dp
                    ),
                    title = category.name,
                    collapsed = collapsed,
                    onClick = {
                        onCategoryClick(category.id)
                    },
                )
            }

            items(categoryChannels) { channel ->
//                if (channel.canView) {
                if (!(channel.id != selectedChannelId && collapsed)) {
                    when (channel) {
                        is DomainChannel.TextChannel -> {
                            WidgetChannelListItem(
                                modifier = Modifier.padding(bottom = 2.dp),
                                title = channel.name,
                                painter = painterResource(R.drawable.ic_tag),
                                selected = selectedChannelId == channel.id,
                                showIndicator = selectedChannelId != channel.id,
                                onClick = {
                                    onChannelSelect(channel.id)
                                },
                            )
                        }
                        is DomainChannel.VoiceChannel -> {
                            WidgetChannelListItem(
                                modifier = Modifier.padding(bottom = 2.dp),
                                title = channel.name,
                                painter = painterResource(R.drawable.ic_volume_up),
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