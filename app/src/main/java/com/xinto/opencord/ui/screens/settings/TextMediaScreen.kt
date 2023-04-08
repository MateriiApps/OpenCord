package com.xinto.opencord.ui.screens.settings

import androidx.compose.foundation.lazy.LazyListScope
import com.xinto.opencord.ui.screens.RadioSetting
import com.xinto.opencord.ui.screens.radioSection
import com.xinto.opencord.ui.screens.section
import com.xinto.opencord.ui.screens.switchSetting

private enum class VideoUpload(override val label: String, override val description: String? = null) : RadioSetting {
    BEST_QUALITY("Best Quality"),
    STANDARD("Standard (recommended)"),
    DATA_SAVER("Data Saver")
}

context(LazyListScope)
fun textMediaScreen() {
    section("Display Images, Videos, and Lolcats") {
        switchSetting(
            label = "When posted as links to chat",
            checked = true,
            onCheckedChange = {},
        )

        switchSetting(
            label = "When uploaded directly to Discord",
            description = "Images larger than 8MB will not be previewed",
            checked = true,
            onCheckedChange = {},
        )

        switchSetting(
            label = "With image descriptions",
            description = "Image descriptions are used to describe images for screenreaders",
            checked = true,
            onCheckedChange = {},
        )
    }

    radioSection(
        label = "Video Uploads",
        value = VideoUpload.STANDARD,
        onOptionClick = { },
    )

    section("Data Consumption") {
        switchSetting(
            label = "Data saving mode",
            description = "Images and videos will be sent in lower quality on cellular networks to reduce data usage",
            checked = true,
            onCheckedChange = {},
        )
    }

    section("Embeds and Link Previews") {
        switchSetting(
            label = "Show embeds and preview website links pasted into chat",
            checked = true,
            onCheckedChange = {},
        )
    }

    section("Emoji") {
        switchSetting(
            label = "Show emoji reactions on messages",
            checked = true,
            onCheckedChange = {},
        )
    }

    section("Stickers") {
        switchSetting(
            label = "Sticker suggestions",
            description = "Allow sticker suggestions to appear when typing messages",
            checked = true,
            onCheckedChange = {},
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
