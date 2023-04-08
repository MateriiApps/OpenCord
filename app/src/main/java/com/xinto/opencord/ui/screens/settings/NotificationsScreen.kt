package com.xinto.opencord.ui.screens.settings

import androidx.compose.foundation.lazy.LazyListScope
import com.xinto.opencord.ui.screens.setting
import com.xinto.opencord.ui.screens.switchSetting

context(LazyListScope)
fun notificationScreen() {
    switchSetting(
        label = "In-app notifications",
        description = "Show notifications in the app",
        checked = true,
        onCheckedChange = {},
    )
    setting(
        label = "System notifications",
        onClick = { /*TODO*/ },
    )
    switchSetting(
        label = "Get notifications when your friends stream",
        checked = false,
        onCheckedChange = {},
    )
}
