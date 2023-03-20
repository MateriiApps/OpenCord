package com.xinto.opencord.ui.screens.settings

import androidx.compose.foundation.lazy.LazyListScope
import com.xinto.opencord.ui.screens.setting
import com.xinto.opencord.ui.screens.switchSetting

context(LazyListScope)
fun languageScreen() {
    setting(
        label = "Language",
        description = "English (United States)",
        onClick = {},
    )

    switchSetting(
        label = "Sync across clients",
        description = "Sync your language settings across all your devices",
        checked = true,
        onCheckedChange = { /*TODO*/ },
    )
}
