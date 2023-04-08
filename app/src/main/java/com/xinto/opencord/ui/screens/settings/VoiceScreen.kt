package com.xinto.opencord.ui.screens.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.ui.screens.section
import com.xinto.opencord.ui.screens.setting
import com.xinto.opencord.ui.screens.sliderSetting
import com.xinto.opencord.ui.screens.switchSetting

context(LazyListScope)
fun voiceScreen() {
    section("Input") {
        setting(
            label = "Input Mode",
            onClick = {},
        ) {
            Text("Voice Activity")
        }

        switchSetting(
            label = "Auto Sensitivity",
            description = "Automatically adjust sensitivity based on your voice",
            checked = false,
            onCheckedChange = {},
        )

        item {
            Slider(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .padding(horizontal = 16.dp),
                value = 0.8f,
                onValueChange = {},
            )
        }
    }

    section("Output") {
        sliderSetting(
            value = 0.6f,
            valueRange = 0f..1f,
            onValueChange = {},
            leadingIcon = R.drawable.ic_volume_mute,
            trailingIcon = R.drawable.ic_volume_up,
        )
    }

    section("Overlay") {
        switchSetting(
            label = "Show overlay",
            description = "See who's talking and access shortcuts while using other apps when connected to voice",
            checked = true,
            onCheckedChange = {},
        )
    }

    section("Voice Processing") {
        switchSetting(
            label = "Echo Cancellation",
            checked = true,
            onCheckedChange = {},
        )
        switchSetting(
            label = "Noise Suppression",
            description = "Reduces background noise",
            checked = true,
            onCheckedChange = {},
        )
        switchSetting(
            label = "Automatic Gain Control",
            description = "Automatically adjusts your microphone's volume",
            checked = true,
            onCheckedChange = {},
        )
        switchSetting(
            label = "Advanced Voice Activity",
            description = "Detects when you're talking and when you're not",
            checked = true,
            onCheckedChange = {},
        )
    }

}
