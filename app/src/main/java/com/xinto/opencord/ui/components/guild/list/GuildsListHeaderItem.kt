package com.xinto.opencord.ui.components.guild.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R

@Composable
fun GuildsListHeaderItem(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(48.dp),
    ) {
        Icon(
            modifier = Modifier
                .size(35.dp),
            tint = MaterialTheme.colorScheme.primary,
            painter = painterResource(R.drawable.ic_discord_logo),
            contentDescription = stringResource(R.string.guilds_home),
        )
    }
}
