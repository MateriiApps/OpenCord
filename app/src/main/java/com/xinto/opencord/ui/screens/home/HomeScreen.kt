package com.xinto.opencord.ui.screens.home

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.xinto.opc.OverlappingPanels
import com.xinto.opc.OverlappingPanelsValue
import com.xinto.opc.rememberOverlappingPanelsState
import com.xinto.opencord.ui.navigation.ChannelsNavigation
import com.xinto.opencord.ui.navigation.ChatNavigation
import com.xinto.opencord.ui.screens.home.panels.channel.ChannelsList
import com.xinto.opencord.ui.screens.home.panels.channel.ChannelsListUnselected
import com.xinto.opencord.ui.screens.home.panels.chat.Chat
import com.xinto.opencord.ui.screens.home.panels.chat.ChatUnselected
import com.xinto.opencord.ui.screens.home.panels.currentuser.CurrentUser
import com.xinto.opencord.ui.screens.home.panels.guild.GuildsList
import com.xinto.opencord.ui.util.animateCornerBasedShapeAsState
import com.xinto.taxi.Taxi
import com.xinto.taxi.rememberNavigator
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit,
    onPinsClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val panelState = rememberOverlappingPanelsState(OverlappingPanelsValue.Closed)

    val coroutineScope = rememberCoroutineScope()

    val channelsNavigator = rememberNavigator<ChannelsNavigation>(initial = ChannelsNavigation.None)
    val chatNavigator = rememberNavigator<ChatNavigation>(initial = ChatNavigation.None)

    Surface(modifier = modifier) {
        OverlappingPanels(
            modifier = Modifier.fillMaxSize(),
            panelsState = panelState,
            panelStart = {
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
                            onGuildSelect = {
                                channelsNavigator.replace(ChannelsNavigation.Channels(it))
                            },
                        )
                        Taxi(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            navigator = channelsNavigator,
                            transitionSpec = { fadeIn() with fadeOut() }
                        ) {
                            when (it) {
                                is ChannelsNavigation.None -> {
                                    ChannelsListUnselected()
                                }
                                is ChannelsNavigation.Channels -> {
                                    ChannelsList(
                                        modifier = Modifier.fillMaxSize(),
                                        onChannelSelect = {
                                            chatNavigator.replace(ChatNavigation.Chat(it))
                                        },
                                        guildId = it.guildId
                                    )
                                }
                            }
                        }
                    }
                    CurrentUser(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp),
                        onSettingsClick = onSettingsClick,
                    )
                }
            },
            panelCenter = {
                val shape by animateCornerBasedShapeAsState(
                    if (panelState.offsetNotZero) {
                        MaterialTheme.shapes.large
                    } else {
                        RoundedCornerShape(0.dp)
                    },
                )
                Taxi(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape),
                    navigator = chatNavigator,
                    transitionSpec = { fadeIn() with fadeOut() }
                ) {
                    when (it) {
                        is ChatNavigation.Chat -> {
                            Chat(
                                modifier = Modifier.fillMaxSize(),
                                onChannelsButtonClick = {
                                    coroutineScope.launch {
                                        panelState.openStartPanel()
                                    }
                                },
                                onMembersButtonClick = {
                                    coroutineScope.launch {
                                        panelState.openEndPanel()
                                    }
                                },
                                onPinsButtonClick = {
                                    onPinsClick(it.channelId)
                                },
                                channelId = it.channelId
                            )
                        }
                        is ChatNavigation.None -> {
                            ChatUnselected()
                        }
                    }
                }
            },
            panelEnd = {
                // members panel
            },
        )
    }
}
