package com.xinto.opencord.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.xinto.opencord.ui.navigation.PinsScreenData
import com.xinto.opencord.ui.screens.home.panels.HomeNavButtons
import com.xinto.opencord.ui.screens.home.panels.channel.HomeChannelsPanel
import com.xinto.opencord.ui.screens.home.panels.chat.HomeChatPanel
import com.xinto.opencord.ui.screens.home.panels.guild.HomeGuildsPanel
import com.xinto.opencord.ui.screens.home.panels.member.MembersList
import com.xinto.opencord.ui.screens.home.panels.user.HomeUserPanel
import com.xinto.opencord.ui.util.animateCornerBasedShapeAsState
import io.github.materiiapps.panels.SwipePanels
import io.github.materiiapps.panels.SwipePanelsValue
import io.github.materiiapps.panels.rememberSwipePanelsState

@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit,
    onPinsClick: (PinsScreenData) -> Unit,
    onSearchClick: () -> Unit,
    onMentionsClick: () -> Unit,
    onFriendsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val panelState = rememberSwipePanelsState()

    BackHandler(enabled = panelState.currentValue != SwipePanelsValue.Center) {
        panelState.close()
    }

    SwipePanels(
        state = panelState,
        inBetweenPadding = 6.dp,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize(),
        start = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .clip(MaterialTheme.shapes.medium)
                    .padding(start = 6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                ) {
                    HomeGuildsPanel(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(72.dp),
                    )
                    HomeChannelsPanel(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                    )
                }
                HomeUserPanel(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 6.dp),
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

            val centerPanelShape by animateCornerBasedShapeAsState(
                if (panelState.currentValue != SwipePanelsValue.Center || panelState.targetValue != SwipePanelsValue.Center) {
                    MaterialTheme.shapes.large
                } else {
                    RoundedCornerShape(0.dp)
                },
            )

            HomeChatPanel(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(centerPanelShape),
            )

        },
        end = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .clip(MaterialTheme.shapes.medium)
                    .padding(end = 6.dp),
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
