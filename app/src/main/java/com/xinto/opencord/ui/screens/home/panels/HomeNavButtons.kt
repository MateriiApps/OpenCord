package com.xinto.opencord.ui.screens.home.panels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R

@Composable
fun HomeNavButtons(
    modifier: Modifier,
    onSearchClick: () -> Unit,
    onMentionsClick: () -> Unit,
    onFriendsClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            6.dp,
            Alignment.CenterHorizontally,
        ),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        val icons = arrayOf(
            Triple(R.drawable.ic_group, R.string.nav_open_friends, onFriendsClick),
            Triple(R.drawable.ic_search, R.string.nav_open_search, onSearchClick),
            Triple(R.drawable.ic_at_sign, R.string.nav_open_mentions, onMentionsClick),
        )

        for ((icon, contentDescription, onClick) in icons) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 1.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = onClick),
                ) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = stringResource(contentDescription),
                        modifier = Modifier
                            .padding(17.dp)
                            .alpha(0.85f),
                    )
                }
            }
        }
    }
}
