package com.xinto.opencord.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.ui.screens.section
import com.xinto.opencord.ui.screens.switchSetting

context(LazyListScope)
fun appearanceSettingsScreen() {
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
        item {
            Row(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_zoom_out),
                    contentDescription = null,
                )

                Slider(
                    modifier = Modifier.weight(1f, true),
                    value = 1f,
                    onValueChange = { /*TODO*/ },
                    valueRange = 0.75f..2f,
                    steps = 10,
                )

                Icon(
                    painter = painterResource(R.drawable.ic_zoom_in),
                    contentDescription = null,
                )
            }
        }
    }

    section("Sync") {
        switchSetting(
            label = "Sync across clients",
            description = "Sync your language settings across all your devices",
            checked = true,
            onCheckedChange = {},
        )
    }
}
