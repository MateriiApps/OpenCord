package com.xinto.opencord.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.ui.component.rememberOCCoilPainter
import com.xinto.opencord.ui.viewmodel.ChannelsViewModel
import com.xinto.opencord.ui.viewmodel.GuildsViewModel
import com.xinto.opencord.ui.widget.WidgetCategory
import com.xinto.opencord.ui.widget.WidgetChannelListItem
import com.xinto.opencord.ui.widget.WidgetGuildListItem
import org.koin.androidx.compose.getViewModel

@Composable
fun GuildsChannelsScreen(
    onGuildSelect: (Long) -> Unit,
    onChannelSelect: (Long) -> Unit,
    guildsViewModel: GuildsViewModel = getViewModel(),
    channelsViewModel: ChannelsViewModel = getViewModel(),
) {
    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        GuildsList(
            modifier = Modifier.fillMaxHeight(),
            onGuildSelect = onGuildSelect,
            viewModel = guildsViewModel
        )
        ChannelsList(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            onChannelSelect = onChannelSelect,
            viewModel = channelsViewModel
        )
    }
}

@Composable
private fun GuildsList(
    onGuildSelect: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GuildsViewModel = getViewModel()
) {
    val discordIcon = painterResource(R.drawable.ic_discord_logo)

    when (viewModel.state) {
        GuildsViewModel.State.Loading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        GuildsViewModel.State.Loaded -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                item {
                    WidgetGuildListItem(
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
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }

                item {
                    Divider(
                        modifier = Modifier
                            .width(32.dp)
                            .clip(MaterialTheme.shapes.medium),
                        thickness = 2.dp
                    )
                }

                items(viewModel.guilds) { guild ->
                    val imagePainter = rememberOCCoilPainter(guild.iconUrl)

                    WidgetGuildListItem(
                        selected = viewModel.selectedGuildId == guild.id,
                        showIndicator = true,
                        onClick = {
                            viewModel.selectGuild(guild.id)
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
        GuildsViewModel.State.Error -> {

        }
    }
}

@Composable
private fun ChannelsList(
    onChannelSelect: (Long) -> Unit,
    viewModel: ChannelsViewModel,
    modifier: Modifier = Modifier
) {
    when (viewModel.state) {
        is ChannelsViewModel.State.Loading -> {

        }
        is ChannelsViewModel.State.Loaded -> {
            LazyColumn(modifier = modifier) {
//                if (bannerUrl != null) {
//                    item {
//                        val painter = rememberOpenCordCachePainter(bannerUrl)
//                        Image(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .heightIn(min = 100.dp, max = 180.dp)
//                                .clip(RoundedCornerShape(8.dp)),
//                            painter = painter,
//                            contentScale = ContentScale.Crop,
//                            contentDescription = "Guild Banner"
//                        )
//                    }
//                }
                for ((category, categoryChannels) in viewModel.channels) {
                    if (category != null) {
                        item {
                            WidgetCategory(category.name)
                        }
                    }
                    items(categoryChannels) { channel ->
                        when (channel) {
                            is DomainChannel.TextChannel -> {
                                WidgetChannelListItem(
                                    title = channel.name,
                                    icon = Icons.Rounded.Tag,
                                    selected = viewModel.selectedChannelId == channel.id,
                                    showIndicator = viewModel.selectedChannelId != channel.id,
                                    onClick = {
                                        viewModel.selectChannel(channel.id)
                                        onChannelSelect(channel.id)
                                    },
                                )
                            }
                            is DomainChannel.VoiceChannel -> {
                                WidgetChannelListItem(
                                    title = channel.name,
                                    icon = Icons.Rounded.VolumeUp,
                                    selected = viewModel.selectedChannelId == channel.id,
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
        is ChannelsViewModel.State.Error -> {

        }
    }
}