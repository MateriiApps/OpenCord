package com.xinto.opencord.ui.screens.home.panels.currentuser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.xinto.opencord.R

@Composable
fun CurrentUserLoading(
    onSettingsClick: () -> Unit
) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.View)
    CurrentUserContent(
        avatar = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmer(shimmer)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)),
            )
        },
        username = {
            Text(
                text = remember { " ".repeat((15..30).random()) },
                modifier = Modifier
                    .shimmer(shimmer)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)),
            )
        },
        discriminator = {
            Text(
                text = " ".repeat(10),
                modifier = Modifier
                    .shimmer(shimmer)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)),
            )
        },
        buttons = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings_open),
                )
            }
        },
        customStatus = null,
    )
}
