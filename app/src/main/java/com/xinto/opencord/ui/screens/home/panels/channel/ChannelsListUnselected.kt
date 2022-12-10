package com.xinto.opencord.ui.screens.home.panels.channel

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.xinto.opencord.R

@Composable
fun ChannelsListUnselected(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        ProvideTextStyle(MaterialTheme.typography.titleMedium) {
            Text(stringResource(R.string.channel_unselected_message))
        }
    }
}
