package com.xinto.opencord.ui.screens.settings

import androidx.compose.foundation.lazy.LazyListScope
import com.xinto.opencord.ui.screens.section
import com.xinto.opencord.ui.screens.switchSetting

context(LazyListScope)
fun friendRequestsScreen() {
    section("Who Can Send You A Friend Request") {
        switchSetting(
            label = "Everyone",
            checked = true,
            onCheckedChange = {},
        )
        switchSetting(
            label = "Friends of Friends",
            checked = true,
            onCheckedChange = {},
        )
        switchSetting(
            label = "Server Members",
            checked = true,
            onCheckedChange = {},
        )
    }
}
