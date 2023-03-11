package com.xinto.opencord.ui.screens.home.panels.guild

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.xinto.opencord.R
import com.xinto.opencord.ui.components.guild.list.GuildsListItemText
import com.xinto.opencord.ui.components.guild.list.RegularGuildItem

@Composable
fun GuildsListLoading(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .verticalScroll(
                state = rememberScrollState(),
                enabled = false,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        RegularGuildItem(
            selected = false,
            showIndicator = false,
            onClick = {},
        ) {
            GuildsListItemText {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.Center),
                    painter = painterResource(R.drawable.ic_discord_logo),
                    contentDescription = stringResource(R.string.guilds_home),
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(bottom = 4.dp, top = 6.dp)
                .clip(MaterialTheme.shapes.medium),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.outline,
        )

        val count = remember { (4..10).random() }
        repeat(count) {
            Box(
                modifier = Modifier
                    .shimmer(shimmer)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
            )
        }
    }
}
