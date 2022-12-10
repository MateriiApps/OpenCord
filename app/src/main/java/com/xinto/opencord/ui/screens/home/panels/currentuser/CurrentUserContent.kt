package com.xinto.opencord.ui.screens.home.panels.currentuser

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CurrentUserContent(
    avatar: @Composable () -> Unit,
    username: @Composable () -> Unit,
    discriminator: @Composable () -> Unit,
    customStatus: (@Composable () -> Unit)?,
    buttons: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    top = 12.dp,
                    bottom = 12.dp,
                    end = 4.dp,
                )
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(modifier = Modifier.size(40.dp)) {
                avatar()
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    ProvideTextStyle(MaterialTheme.typography.titleSmall) {
                        username()
                    }
                    if (customStatus != null) {
                        ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                            discriminator()
                        }
                    }
                }
                ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                    if (customStatus != null) {
                        customStatus()
                    } else {
                        discriminator()
                    }
                }
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                content = buttons,
            )
        }
    }
}
