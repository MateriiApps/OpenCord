package com.xinto.opencord.ui.screens.home.panels.chat.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R

@Composable
fun HomeChatPanelTopBar(
    channelName: String,
    onChannelsButtonClick: () -> Unit,
    onPinsButtonClick: () -> Unit,
    onMembersButtonClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (channelName.isNotEmpty()) {
                    Icon(
                        painter = painterResource(R.drawable.ic_tag),
                        contentDescription = null,
                        modifier = Modifier,
                    )
                }
                Text(
                    text = channelName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        },
        navigationIcon = {
            IconButton(onChannelsButtonClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_menu),
                    contentDescription = null,
                )
            }
        },
        actions = {
            IconButton(onClick = onPinsButtonClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_pin),
                    contentDescription = null,
                )
            }
            IconButton(onMembersButtonClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_people),
                    contentDescription = null,
                )
            }
        },
    )
}
