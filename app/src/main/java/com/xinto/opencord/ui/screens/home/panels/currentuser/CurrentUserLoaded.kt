package com.xinto.opencord.ui.screens.home.panels.currentuser

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.domain.usersettings.DomainCustomStatus
import com.xinto.opencord.domain.usersettings.DomainUserStatus
import com.xinto.opencord.ui.components.OCBadgeBox
import com.xinto.opencord.ui.components.OCImage
import com.xinto.opencord.ui.components.indicator.UserStatusIcon
import com.xinto.opencord.ui.util.ifNotNullComposable

@Composable
fun CurrentUserLoaded(
    onSettingsClick: () -> Unit,
    avatarUrl: String,
    username: String,
    discriminator: String,
    status: DomainUserStatus?,
    isStreaming: Boolean,
    customStatus: DomainCustomStatus?,
) {
    CurrentUserContent(
        avatar = {
            OCBadgeBox(
                badge = status.ifNotNullComposable { userStatus ->
                    UserStatusIcon(
                        modifier = Modifier.size(10.dp),
                        isStreaming = isStreaming,
                        userStatus = userStatus,
                    )
                },
            ) {
                OCImage(
                    url = avatarUrl,
                    memoryCaching = true,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )
            }
        },
        username = { Text(username) },
        discriminator = { Text(discriminator) },
        customStatus = customStatus?.text?.ifNotNullComposable { Text(it) },
        buttons = {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.settings_open),
                )
            }
        },
    )
}
