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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.discord.panels.OverlappingPanelsState
import com.xinto.opencord.R
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.network.result.DiscordAPIResult
import com.xinto.opencord.ui.component.image.rememberOpenCordCachePainter
import com.xinto.opencord.ui.component.layout.OpenCordBackground
import com.xinto.opencord.ui.component.list.ChannelItem
import com.xinto.opencord.ui.component.list.GuildItem
import com.xinto.opencord.ui.component.list.ListCategoryItem
import com.xinto.opencord.ui.component.text.OpenCordText
import com.xinto.opencord.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalCoilApi::class, ExperimentalMaterialApi::class)
@Composable
fun StartPanel(
    viewModel: MainViewModel,
    panelState: OverlappingPanelsState,
) {
    val coroutineScope = rememberCoroutineScope()

    val guildsResult by viewModel.guilds.collectAsState()
    val channelsResult by viewModel.currentGuildChannels.collectAsState()

    val currentGuild by viewModel.currentGuild.collectAsState()
    val currentChannel by viewModel.currentChannel.collectAsState()

    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        OpenCordBackground(
            modifier = Modifier.fillMaxHeight(),
            backgroundColorAlpha = 0.3f
        ) {
            when (val result: DiscordAPIResult<List<DomainGuild>> = guildsResult) {
                is DiscordAPIResult.Loading -> {
                    CircularProgressIndicator()
                }
                is DiscordAPIResult.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        item {
                            val discordIcon = painterResource(R.drawable.ic_discord_logo)
                            GuildItem(
                                selected = false,
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
                                    .padding(vertical = 4.dp)
                                    .clip(MaterialTheme.shapes.medium),
                                thickness = 2.dp
                            )
                        }

                        items(result.data) { guild ->
                            val imagePainter = rememberOpenCordCachePainter(guild.iconUrl)

                            GuildItem(
                                selected = currentGuild == guild,
                                onClick = {
                                    viewModel.setCurrentGuild(guild)
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
                    OpenCordText(text = "Failed to load guilds")
                }
            }
        }
        OpenCordBackground(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            backgroundColorAlpha = 0.6f
        ) {
            when (val result: DiscordAPIResult<MainViewModel.ChannelListData> = channelsResult) {
                is DiscordAPIResult.Loading -> {
                    CircularProgressIndicator()
                }
                is DiscordAPIResult.Success -> {
                    Crossfade(result.data) { channelData ->
                        LazyColumn {
                            item {
                                val bannerUrl = channelData.bannerUrl

                                if (bannerUrl != null) {
                                    val painter = rememberOpenCordCachePainter(bannerUrl)
                                    Image(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .heightIn(min = 100.dp, max = 150.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        painter = painter,
                                        contentScale = ContentScale.Crop,
                                        contentDescription = "Guild Banner"
                                    )
                                }
                            }

                            channelData.channels.forEach { (category, channels) ->
                                if (category != null) {
                                    item {
                                        ListCategoryItem(text = category.name)
                                    }
                                }
                                items(channels) { channel ->
                                    when (channel) {
                                        is DomainChannel.TextChannel -> {
                                            ChannelItem(
                                                title = channel.channelName,
                                                icon = Icons.Rounded.Tag,
                                                onClick = {
                                                    coroutineScope.launch {
                                                        panelState.closePanels()
                                                    }
                                                    viewModel.setCurrentChannel(channel)
                                                },
                                                selected = currentChannel == channel
                                            )
                                        }
                                        is DomainChannel.VoiceChannel -> {
                                            ChannelItem(
                                                title = channel.channelName,
                                                icon = Icons.Rounded.VolumeUp,
                                                onClick = {

                                                },
                                                selected = currentChannel == channel
                                            )
                                        }
                                        else -> Unit
                                    }
                                }
                            }
                        }
                    }
                }
                is DiscordAPIResult.Error -> {

                }
            }
        }
    }
}