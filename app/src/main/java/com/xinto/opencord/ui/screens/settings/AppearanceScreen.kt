package com.xinto.opencord.ui.screens.settings

import androidx.compose.foundation.lazy.LazyListScope
import com.xinto.opencord.R
import com.xinto.opencord.ui.screens.section
import com.xinto.opencord.ui.screens.sliderSetting
import com.xinto.opencord.ui.screens.switchSetting

context(LazyListScope)
fun appearanceScreen() {
    section("Theme") {
        switchSetting(
            label = "Dark mode",
            description = "Use dark mode for the app",
            checked = true,
            onCheckedChange = {},
        )

        switchSetting(
            label = "Dynamic colors",
            description = "Use dynamic colors for the app",
            checked = true,
            onCheckedChange = {},
        )
    }

    section("Zoom level") {
        sliderSetting(
            value = 1f,
            valueRange = 0.75f..2f,
            onValueChange = {},
            leadingIcon = R.drawable.ic_zoom_out,
            trailingIcon = R.drawable.ic_zoom_in,
        )
    }

    section("Sync") {
        switchSetting(
            label = "Sync across clients",
            checked = true,
            onCheckedChange = {},
        )
    }
}
