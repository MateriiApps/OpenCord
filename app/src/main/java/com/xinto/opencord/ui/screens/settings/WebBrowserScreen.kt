package com.xinto.opencord.ui.screens.settings

import androidx.compose.foundation.lazy.LazyListScope
import com.xinto.opencord.ui.screens.switchSetting

context(LazyListScope)
fun webBrowserScreen() {
    switchSetting(
        label = "Open links in the app",
        description = "Links will open in the app instead of your default browser",
        checked = true,
        onCheckedChange = {},
    )
}
