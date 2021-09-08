package com.xinto.opencord.ui.screens.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.network.result.DiscordAPIResult
import com.xinto.opencord.ui.component.layout.OpenCordBackground
import com.xinto.opencord.ui.component.list.OpenCordChannelListItem
import com.xinto.opencord.ui.component.text.OpenCordListCategory
import com.xinto.opencord.ui.component.text.OpenCordText
import com.xinto.opencord.ui.viewmodel.MainViewModel
import com.xinto.opencord.ui.widgets.guild.ClickableGuildIcon
import com.xinto.opencord.util.getSortedChannels

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LeftPanel(
    viewModel: MainViewModel
) {
    val guildsResult by viewModel.guilds.collectAsState()
    val currentGuild by viewModel.currentGuild.collectAsState()

    val channelsResult by viewModel.currentGuildChannels.collectAsState()

    OpenCordBackground(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
        ) {
            when (val result: DiscordAPIResult<List<DomainGuild>> = guildsResult) {
                is DiscordAPIResult.Loading -> {
                    CircularProgressIndicator()
                }
                is DiscordAPIResult.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(result.data) { guild ->
                            ClickableGuildIcon(
                                iconUrl = guild.iconUrl,
                                selected = currentGuild == guild,
                                onClick = {
                                    viewModel.setCurrentGuild(guild)
                                }
                            )
                        }
                    }
                }
                is DiscordAPIResult.Error -> {
                    OpenCordText(text = "Failed to load guilds")
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp)
                    .width(2.dp)
            )
            when (val result: DiscordAPIResult<SnapshotStateList<DomainChannel>> = channelsResult) {
                is DiscordAPIResult.Loading -> {
                    CircularProgressIndicator()
                }
                is DiscordAPIResult.Success -> {
                    val sortedChannels = getSortedChannels(result.data)
                    Crossfade(targetState = sortedChannels) { channels ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                        ) {
                            channels.forEach { (category, channels) ->
                                if (category != null) {
                                    item {
                                        OpenCordListCategory(text = category.name)
                                    }
                                }
                                items (channels) { channel ->
                                    when (channel) {
                                        is DomainChannel.TextChannel -> {
                                            OpenCordChannelListItem(
                                                title = channel.channelName,
                                                icon = Icons.Rounded.Tag,
                                                onClick = {
                                                    viewModel.setCurrentChannel(channel)
                                                }
                                            )
                                        }
                                        is DomainChannel.VoiceChannel -> {
                                            OpenCordChannelListItem(
                                                title = channel.channelName,
                                                icon = Icons.Rounded.VolumeUp,
                                                onClick = {

                                                }
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