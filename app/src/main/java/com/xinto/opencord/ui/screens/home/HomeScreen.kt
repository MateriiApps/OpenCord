package com.xinto.opencord.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.xinto.opencord.ui.navigation.PinsScreenData
import com.xinto.opencord.ui.screens.home.panels.HomeNavButtons
import com.xinto.opencord.ui.screens.home.panels.channel.ChannelsList
import com.xinto.opencord.ui.screens.home.panels.chat.Chat
import com.xinto.opencord.ui.screens.home.panels.currentuser.CurrentUser
import com.xinto.opencord.ui.screens.home.panels.guild.GuildsList
import com.xinto.opencord.ui.screens.home.panels.member.MembersList
import com.xinto.opencord.ui.util.animateCornerBasedShapeAsState
import com.xinto.opencord.ui.viewmodel.ChannelsViewModel
import com.xinto.opencord.ui.viewmodel.ChatViewModel
import com.xinto.opencord.ui.viewmodel.CurrentUserViewModel
import com.xinto.opencord.ui.viewmodel.GuildsViewModel
import io.github.materiiapps.panels.PanelsDefaults
import io.github.materiiapps.panels.SwipePanels
import io.github.materiiapps.panels.SwipePanelsValue
import io.github.materiiapps.panels.rememberSwipePanelsState
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit,
    onPinsClick: (PinsScreenData) -> Unit,
    onSearchClick: () -> Unit,
    onMentionsClick: () -> Unit,
    onFriendsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentUserViewModel: CurrentUserViewModel = getViewModel()
    val chatViewModel: ChatViewModel = getViewModel()
    val guildsViewModel: GuildsViewModel = getViewModel()
    val channelsViewModel: ChannelsViewModel = getViewModel()

    val channelsListState = rememberLazyListState()
    val panelState = rememberSwipePanelsState()

    BackHandler(enabled = panelState.currentValue != SwipePanelsValue.Center) {
        panelState.close()
    }

    val centerPanelShape by animateCornerBasedShapeAsState(
        if (panelState.currentValue != SwipePanelsValue.Center || panelState.targetValue != SwipePanelsValue.Center) {
            MaterialTheme.shapes.large
        } else {
            RoundedCornerShape(0.dp)
        },
    )

    SwipePanels(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize(),
        state = panelState,
        colors = PanelsDefaults.colors(
            startPanelBackground = MaterialTheme.colorScheme.surface,
            centerPanelBackground = MaterialTheme.colorScheme.surface,
            endPanelBackground = MaterialTheme.colorScheme.surface,
        ),
        shapes = PanelsDefaults.shapes(
            startPanelShape = MaterialTheme.shapes.medium,
            centerPanelShape = centerPanelShape,
            endPanelShape = MaterialTheme.shapes.medium,
        ),
        paddings = PanelsDefaults.paddings(
            startPanelPadding = PaddingValues(end = 6.dp),
            endPanelPadding = PaddingValues(start = 6.dp),
        ),
        start = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                ) {
                    GuildsList(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(72.dp),
                        onGuildSelect = channelsViewModel::load,
                        viewModel = guildsViewModel,
                    )
                    ChannelsList(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        onChannelSelect = chatViewModel::load,
                        viewModel = channelsViewModel,
                        lazyListState = channelsListState,
                    )
                }
                CurrentUser(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp),
                    viewModel = currentUserViewModel,
                    onSettingsClick = onSettingsClick,
                )
            }

        },
        center = {
            if (panelState.currentValue != SwipePanelsValue.Center) {
                Box(
                    modifier = Modifier
                        .zIndex(1f)
                        .fillMaxSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = panelState::close,
                        ),
                )
            }

            Chat(
                onChannelsButtonClick = panelState::openStart,
                onMembersButtonClick = panelState::openEnd,
                onPinsButtonClick = {
                    onPinsClick(PinsScreenData(channelsViewModel.selectedChannelId))
                },
                viewModel = chatViewModel,
                modifier = Modifier
                    .fillMaxSize(),
            )

        },
        end = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                MembersList(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                )
                HomeNavButtons(
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .height(60.dp)
                        .fillMaxWidth(),
                    onFriendsClick = onFriendsClick,
                    onMentionsClick = onMentionsClick,
                    onSearchClick = onSearchClick,
                )
            }
        },
    )
}
