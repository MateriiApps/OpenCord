package com.xinto.opencord.ui.screens.mentions.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.ui.util.ContentAlpha


@Composable
fun MentionsTopBar(
    includeAllServers: Boolean,
    currentGuildName: String?,
    onBackClick: () -> Unit,
    onFilterClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Recent mentions",
                    modifier = Modifier,
                )

                val serverName = if (!includeAllServers && currentGuildName != null) {
                    currentGuildName
                } else {
                    "All servers"
                }

                AnimatedContent(
                    targetState = serverName,
                    transitionSpec = {
                        slideIntoContainer(AnimatedContentScope.SlideDirection.Up) with slideOutOfContainer(AnimatedContentScope.SlideDirection.Up)
                    },
                ) {
                    Text(
                        text = serverName,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .alpha(ContentAlpha.medium)
                            .offset(y = (-2).dp)
                            .padding(bottom = 1.dp),
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.navigation_back),
                )
            }
        },
        actions = {
            IconButton(onClick = onFilterClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_filter),
                    contentDescription = "Open mention filters",
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
