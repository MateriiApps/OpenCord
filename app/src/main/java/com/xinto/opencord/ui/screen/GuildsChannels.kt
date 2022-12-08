package com.xinto.opencord.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.xinto.opencord.R
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainCustomStatus
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.domain.model.DomainUserStatus
import com.xinto.opencord.ui.component.OCAsyncImage
import com.xinto.opencord.ui.component.OCBadgeBox
import com.xinto.opencord.ui.util.ContentAlpha
import com.xinto.opencord.ui.util.ProvideContentAlpha
import com.xinto.opencord.ui.viewmodel.ChannelsViewModel
import com.xinto.opencord.ui.viewmodel.CurrentUserViewModel
import com.xinto.opencord.ui.viewmodel.GuildsViewModel
import com.xinto.opencord.ui.widget.*
import com.xinto.opencord.util.ifNotNullComposable
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
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(
            modifier = Modifier.weight(1f),
        ) {
            GuildsList(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(72.dp),
                onGuildSelect = onGuildSelect,
                viewModel = guildsViewModel,
            )
            ChannelsList(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                onChannelSelect = onChannelSelect,
                viewModel = channelsViewModel,
            )
        }
        CurrentUserItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp),
            viewModel = currentUserViewModel,
            onSettingsClick = onSettingsClick,
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
                selectedGuildId = viewModel.selectedGuildId,
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
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is ChannelsViewModel.State.Loading -> {
                    ChannelsListLoading(
                        modifier = Modifier.fillMaxSize(),
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
                        selectedChannelId = viewModel.selectedChannelId,
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
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
    viewModel: CurrentUserViewModel = getViewModel()
) {
    var showStatusSheet by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        onClick = { showStatusSheet = true },
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp,
    ) {
        when (viewModel.state) {
            CurrentUserViewModel.State.Loading -> {
                CurrentUserItemLoading(
                    onSettingsClick = onSettingsClick,
                )
            }
            CurrentUserViewModel.State.Loaded -> {
                CurrentUserItemLoaded(
                    onSettingsClick = onSettingsClick,
                    avatarUrl = viewModel.avatarUrl,
                    username = viewModel.username,
                    discriminator = viewModel.discriminator,
                    status = viewModel.userStatus,
                    isStreaming = viewModel.isStreaming,
                    customStatus = viewModel.userCustomStatus,
                )
            }
            CurrentUserViewModel.State.Error -> {

            }
        }
    }

    if (showStatusSheet) {
        CurrentUserSheet(
            onClose = { showStatusSheet = false },
        )
    }
}

@Composable
private fun CurrentUserItemLoading(
    onSettingsClick: () -> Unit
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    WidgetCurrentUser(
        avatar = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer(shimmer)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)),
            )
        },
        username = {
            val spaces = remember { (15..30).random() }
            Text(
                text = " ".repeat(spaces),
                modifier = Modifier
                    .shimmer(shimmer)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)),
            )
        },
        discriminator = {
            Text(
                text = " ".repeat(10),
                modifier = Modifier
                    .shimmer(shimmer)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)),
            )
        },
        buttons = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings_open),
                )
            }
        },
        customStatus = null,
    )
}

@Composable
private fun CurrentUserItemLoaded(
    onSettingsClick: () -> Unit,
    avatarUrl: String,
    username: String,
    discriminator: String,
    status: DomainUserStatus?,
    isStreaming: Boolean,
    customStatus: DomainCustomStatus?,
) {
    WidgetCurrentUser(
        avatar = {
            OCBadgeBox(
                badge = status.ifNotNullComposable { userStatus ->
                    WidgetStatusIcon(
                        modifier = Modifier.size(10.dp),
                        isStreaming = isStreaming,
                        userStatus = userStatus,
                    )
                },
            ) {
                OCAsyncImage(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    url = avatarUrl,
                )
            }
        },
        username = { Text(username) },
        discriminator = { Text(discriminator) },
        customStatus = customStatus?.text?.ifNotNullComposable { Text(it) },
        buttons = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings_open),
                )
            }
        },
    )
}

@Composable
private fun GuildsListLoading(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .verticalScroll(
                state = rememberScrollState(),
                enabled = false,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        WidgetGuildListItem(
            selected = false,
            showIndicator = false,
            onClick = {},
        ) {
            WidgetGuildContentVector {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.Center),
                    painter = painterResource(R.drawable.ic_discord_logo),
                    contentDescription = stringResource(R.string.guilds_home),
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth(0.55f)
                .padding(bottom = 4.dp)
                .clip(MaterialTheme.shapes.medium),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.outline,
        )

        val count = remember { (4..10).random() }
        repeat(count) {
            Box(
                modifier = Modifier
                    .shimmer(shimmer)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
            )
        }
    }
}

@Composable
private fun GuildsListLoaded(
    onGuildSelect: (Long) -> Unit,
    selectedGuildId: Long,
    guilds: List<DomainGuild>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        item {
            WidgetGuildListItem(
                modifier = Modifier.fillParentMaxWidth(),
                selected = false,
                showIndicator = false,
                onClick = {},
            ) {
                WidgetGuildContentVector {
                    Icon(
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.Center),
                        painter = painterResource(R.drawable.ic_discord_logo),
                        contentDescription = stringResource(R.string.guilds_home),
                    )
                }
            }
        }

        item {
            Divider(
                modifier = Modifier
                    .fillParentMaxWidth(0.55f)
                    .padding(bottom = 4.dp)
                    .clip(MaterialTheme.shapes.medium),
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.outline,
            )
        }

        items(guilds) { guild ->
            WidgetGuildListItem(
                modifier = Modifier.fillParentMaxWidth(),
                selected = selectedGuildId == guild.id,
                showIndicator = true,
                onClick = {
                    onGuildSelect(guild.id)
                },
            ) {
                if (guild.iconUrl != null) {
                    WidgetGuildContentImage(
                        url = guild.iconUrl,
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
        contentAlignment = Alignment.Center,
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
                WidgetCategory(
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
                WidgetChannelListItem(
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

@Composable
private fun ChannelsListLoaded(
    onChannelSelect: (Long) -> Unit,
    onCategoryClick: (Long) -> Unit,
    selectedChannelId: Long,
    bannerUrl: String?,
    boostLevel: Int,
    guildName: String,
    channels: Map<DomainChannel.Category?, List<DomainChannel>>,
    collapsedCategories: List<Long>,
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
                        WidgetCategory(
                            modifier = Modifier.padding(
                                top = 12.dp,
                                bottom = 4.dp,
                            ),
                            title = { Text(category.capsName) },
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
                        is DomainChannel.TextChannel -> {
                            WidgetChannelListItem(
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
                        is DomainChannel.VoiceChannel -> {
                            WidgetChannelListItem(
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
