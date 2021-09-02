package com.xinto.opencord.ui.screens.main

import android.util.Log
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.network.result.DiscordAPIResult
import com.xinto.opencord.ui.component.layout.OpenCordBackground
import com.xinto.opencord.ui.component.list.OpenCordChannelListItem
import com.xinto.opencord.ui.component.text.OpenCordListCategory
import com.xinto.opencord.ui.viewmodel.MainViewModel
import com.xinto.opencord.ui.widgets.guild.ClickableGuildIcon
import com.xinto.opencord.util.getSortedChannels
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LeftPanel() {
    val viewModel: MainViewModel = getViewModel()
    val coroutineScope = rememberCoroutineScope()

    val guilds by viewModel.guilds.collectAsState()
    val currentGuild by viewModel.currentGuild.collectAsState()

    val channels = getSortedChannels(currentGuild?.channels ?: emptyList())

    LaunchedEffect(key1 = viewModel) {
        coroutineScope.launch {
            viewModel.fetchGuilds()
        }
    }

    OpenCordBackground {
        when (val guildsResult: DiscordAPIResult<List<DomainGuild>> = guilds) {
            is DiscordAPIResult.Loading -> {
                CircularProgressIndicator()
            }
            is DiscordAPIResult.Success -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        items(guildsResult.result) { guild ->
                            ClickableGuildIcon(
                                iconUrl = guild.iconUrl,
                                selected = currentGuild == guild,
                                onClick = {
                                    viewModel.setCurrentGuild(guild)
                                }
                            )
                        }
                    }
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 8.dp)
                            .width(2.dp)
                    )
                    Crossfade(targetState = channels) { channels ->
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
                                                title = channel.name,
                                                icon = Icons.Rounded.Tag,
                                                onClick = {

                                                }
                                            )
                                        }
                                        is DomainChannel.VoiceChannel -> {
                                            OpenCordChannelListItem(
                                                title = channel.name,
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
            }
            is DiscordAPIResult.Error -> {
                Log.e("guilds", guildsResult.e.stackTraceToString())
            }
        }

    }
}