package com.xinto.opencord.ui.screens.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.xinto.opencord.R
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.network.result.DiscordAPIResult
import com.xinto.opencord.ui.component.image.rememberOpenCordCachePainter
import com.xinto.opencord.ui.component.layout.OpenCordBackground
import com.xinto.opencord.ui.component.list.ChannelItem
import com.xinto.opencord.ui.component.list.GuildItem
import com.xinto.opencord.ui.component.list.ListCategoryItem
import com.xinto.opencord.ui.component.text.Text
import com.xinto.opencord.ui.theme.topLargeCorners
import com.xinto.opencord.ui.viewmodel.MainViewModel
import com.xinto.overlappingpanels.OverlappingPanelsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class, ExperimentalMaterialApi::class)
@Composable
fun StartPanel(
    viewModel: MainViewModel,
    panelState: OverlappingPanelsState,
) {
    val coroutineScope = rememberCoroutineScope()

    val guildsResult = viewModel.guilds

    val currentGuild = viewModel.currentGuild
    val currentChannel = viewModel.currentChannel

    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        when (val result: DiscordAPIResult<List<DomainGuild>> = guildsResult) {
            is DiscordAPIResult.Loading -> {
                CircularProgressIndicator()
            }
            is DiscordAPIResult.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    item {
                        val discordIcon = painterResource(R.drawable.ic_discord_logo)
                        GuildItem(
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

                    items(result.data) { guild ->
                        val imagePainter = rememberOpenCordCachePainter(guild.iconUrl)

                        GuildItem(
                            selected = currentGuild is MainViewModel.CurrentGuild.Guild
                                    && currentGuild.data.id == guild.id,
                            showIndicator = true,
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.setCurrentGuild(guild)
                                }
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
            is DiscordAPIResult.Error -> {
                Text(text = "Failed to load guilds")
            }
        }
        OpenCordBackground(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.topLargeCorners),
            backgroundColorAlpha = 0.6f
        ) {
            Crossfade(currentGuild) { currentGuild ->
                when (currentGuild) {
                    is MainViewModel.CurrentGuild.Guild -> {
                        val guildChannelData = viewModel.channels[currentGuild.data.id]
                        val bannerUrl = guildChannelData?.bannerUrl
                        LazyColumn {
                            if (bannerUrl != null) {
                                item {
                                    val painter = rememberOpenCordCachePainter(bannerUrl)
                                    Image(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 100.dp, max = 180.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        painter = painter,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Guild Banner"
                                    )
                                }
                            }
                            if (guildChannelData != null) {
                                for ((category, categoryChannels) in guildChannelData.channels) {
                                    if (category != null) {
                                        item {
                                            ListCategoryItem(text = category.name)
                                        }
                                    }
                                    items(categoryChannels) { channel ->
                                        when (channel) {
                                            is DomainChannel.TextChannel -> {
                                                ChannelItem(
                                                    title = channel.channelName,
                                                    icon = Icons.Rounded.Tag,
                                                    selected = currentChannel is MainViewModel.CurrentChannel.Channel
                                                            && currentChannel.data.channelId == channel.id,
                                                    showIndicator = currentChannel is MainViewModel.CurrentChannel.Channel
                                                            && currentChannel.data.channelId != channel.id,
                                                    onClick = {
                                                        coroutineScope.launch {
                                                            panelState.closePanels()
                                                            viewModel.setCurrentChannel(channel)
                                                        }
                                                    },
                                                )
                                            }
                                            is DomainChannel.VoiceChannel -> {
                                                ChannelItem(
                                                    title = channel.channelName,
                                                    icon = Icons.Rounded.VolumeUp,
                                                    selected = currentChannel == channel,
                                                    showIndicator = false,
                                                    onClick = {

                                                    },
                                                )
                                            }
                                            else -> Unit
                                        }
                                    }
                                }
                            } else {
                                item {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                    is MainViewModel.CurrentGuild.None -> {

                    }
                }
            }
        }
    }
}