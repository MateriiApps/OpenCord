package com.xinto.opencord.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.rest.dto.ApiStatus

@Composable
fun AvatarStatus(avatarUrl: String, userStatus: ApiStatus?) {
    val userIcon = rememberOCCoilPainter(avatarUrl)

    Box {
        Image(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            painter = userIcon,
            contentDescription = null
        )

        if (userStatus != null) {
            val statusIcon = when (userStatus) {
                ApiStatus.Online -> R.drawable.ic_status_online
                ApiStatus.Invisible -> R.drawable.ic_status_invisible
                ApiStatus.Idle -> R.drawable.ic_status_idle
                ApiStatus.Dnd -> R.drawable.ic_status_dnd
            }

            Surface(
                shape = CircleShape,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    painter = painterResource(statusIcon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(3.dp)
                )
            }
        }
    }
}
