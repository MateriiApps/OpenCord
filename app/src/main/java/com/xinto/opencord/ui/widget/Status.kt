package com.xinto.opencord.ui.widget

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.xinto.opencord.R
import com.xinto.opencord.domain.usersettings.DomainUserStatus

@Composable
fun WidgetStatusIcon(
    userStatus: DomainUserStatus,
    isStreaming: Boolean,
    modifier: Modifier = Modifier,
) {
    val statusIcon = if (isStreaming)
        R.drawable.ic_status_streaming
    else when (userStatus) {
        DomainUserStatus.Online -> R.drawable.ic_status_online
        DomainUserStatus.Invisible -> R.drawable.ic_status_invisible
        DomainUserStatus.Idle -> R.drawable.ic_status_idle
        DomainUserStatus.Dnd -> R.drawable.ic_status_dnd
    }

    Icon(
        modifier = modifier,
        painter = painterResource(statusIcon),
        contentDescription = null,
        tint = Color.Unspecified,
    )
}
