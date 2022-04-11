package com.xinto.opencord.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainMeGuild
import com.xinto.opencord.ui.component.rememberOCCoilPainter
import com.xinto.opencord.ui.viewmodel.ChannelsViewModel
import com.xinto.opencord.ui.viewmodel.GuildsViewModel
import com.xinto.opencord.ui.widget.WidgetCategory
import com.xinto.opencord.ui.widget.WidgetChannelListItem
import com.xinto.opencord.ui.widget.WidgetGuildListItem
import org.koin.androidx.compose.getViewModel

@Composable
fun GuildsChannelsScreen(
    onGuildSelect: () -> Unit,
    onChannelSelect: () -> Unit,
    guildsViewModel: GuildsViewModel = getViewModel(),
    channelsViewModel: ChannelsViewModel = getViewModel(),
) {
    Row(
        modifier = Modifier.fillMaxSize(),
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
                guilds = viewModel.guilds,
                selectedGuildId = viewModel.selectedGuildId
            )
        }
        GuildsViewModel.State.Error -> {

        }
    }
}

@Composable
fun GuildsListLoading(
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
fun GuildsListLoaded(
    onGuildSelect: (Long) -> Unit,
    selectedGuildId: Long,
    guilds: List<DomainMeGuild>,
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
                    tint = MaterialTheme.colorScheme.primary
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
            val imagePainter = rememberOCCoilPainter(guild.iconUrl)

            WidgetGuildListItem(
                selected = selectedGuildId == guild.id,
                showIndicator = true,
                onClick = {
                    onGuildSelect(guild.id)
                }
            ) {
                Image(
                    modifier = Modifier.size(48.dp),
                    painter = imagePainter,
                    contentDescription = "Guild Icon"
                )
            }
        }
    }
}

@Composable
private fun ChannelsList(
    onChannelSelect: () -> Unit,
    viewModel: ChannelsViewModel,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        tonalElevation = 1.dp
    ) {
        when (viewModel.state) {
            is ChannelsViewModel.State.Loading -> {

            }
            is ChannelsViewModel.State.Loaded -> {
                ChannelsListLoaded(
                    modifier = Modifier.fillMaxSize(),
                    onChannelSelect = {
                        viewModel.selectChannel(it)
                        onChannelSelect()
                    },
                    bannerUrl = viewModel.guildBannerUrl,
                    guildName = viewModel.guildName,
                    channels = viewModel.channels,
                    selectedChannelId = viewModel.selectedChannelId
                )
            }
            is ChannelsViewModel.State.Error -> {

            }
        }
    }
}

@Composable
private fun ChannelsListLoaded(
    onChannelSelect: (Long) -> Unit,
    selectedChannelId: Long,
    bannerUrl: String?,
    guildName: String,
    channels: Map<DomainChannel.Category?, List<DomainChannel>>,
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
                ProvideTextStyle(MaterialTheme.typography.titleLarge) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        text = guildName,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
        for ((category, categoryChannels) in channels) {
            if (category != null) {
                item {
                    WidgetCategory(
                        modifier = Modifier.padding(
                            start = 6.dp,
                            top = 12.dp,
                            bottom = 4.dp
                        ),
                        title = category.name
                    )
                }
            }
            items(categoryChannels) { channel ->
                when (channel) {
                    is DomainChannel.TextChannel -> {
                        WidgetChannelListItem(
                            modifier = Modifier.padding(vertical = 2.dp),
                            title = channel.name,
                            icon = Icons.Rounded.Tag,
                            selected = selectedChannelId == channel.id,
                            showIndicator = selectedChannelId != channel.id,
                            onClick = {
                                onChannelSelect(channel.id)
                            },
                        )
                    }
                    is DomainChannel.VoiceChannel -> {
                        WidgetChannelListItem(
                            modifier = Modifier.padding(vertical = 2.dp),
                            title = channel.name,
                            icon = Icons.Rounded.VolumeUp,
                            selected = false,
                            showIndicator = false,
                            onClick = {

                            },
                        )
                    }
                    else -> Unit
                }
            }
        }
    }
}