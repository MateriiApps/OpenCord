package com.xinto.opencord.ui.screens.settings

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.RadioButton
import com.xinto.opencord.ui.screens.section
import com.xinto.opencord.ui.screens.setting
import com.xinto.opencord.ui.screens.switchSetting

context(LazyListScope)
fun privacySafetySettingsScreen() {
    section("Safe Direct Messaging") {
        setting(
            label = "Keep me safe",
            description = "Scan direct messages from everyone",
            onClick = {},
            trailingContent = {
                RadioButton(
                    selected = true,
                    onClick = { /*TODO*/ },
                )
            },
        )

        setting(
            label = "My friends are nice",
            description = "Scan direct messages from everyone unless they are a friend",
            onClick = {},
            trailingContent = {
                RadioButton(
                    selected = false,
                    onClick = { /*TODO*/ },
                )
            },
        )

        setting(
            label = "Do not scan",
            description = "Direct messages will not be scanned for explicit content",
            onClick = {},
            trailingContent = {
                RadioButton(
                    selected = false,
                    onClick = { /*TODO*/ },
                )
            },
        )
    }

    section("Server Privacy Defaults") {
        switchSetting(
            label = "Allow direct messages from server members",
            checked = true,
            onCheckedChange = {},
        )
        switchSetting(
            label = "Allow access to age-restricted commands from apps in Direct Messages",
            checked = true,
            onCheckedChange = {},
        )
    }

    section("Activity Status") {
        switchSetting(
            label = "Display current activity as status message",
            description = "Discord will automatically update your status if you're attending a public stage",
            checked = true,
            onCheckedChange = {},
        )
    }

    section("Message Requests") {
        switchSetting(
            label = "Enable message requests from server members you may not know",
            checked = true,
            onCheckedChange = {},
        )
    }

    section("Find Your Friends") {
        switchSetting(
            label = "Sync Contacts",
            description = "Sync your contacts to find friends on Discord",
            checked = true,
            onCheckedChange = {},
        )
    }

    section("Discovery Permissions") {
        switchSetting(
            label = "Phone Number",
            description = "People can add you by phone number",
            checked = true,
            onCheckedChange = {},
        )
        switchSetting(
            label = "Email Address",
            description = "People can add you by email address",
            checked = true,
            onCheckedChange = {},
        )
    }

    section("How We Use Your Data") {
        switchSetting(
            label = "Use data to improve Discord",
            checked = true,
            onCheckedChange = {},
        )
        switchSetting(
            label = "Use data to customize my Discord experience",
            checked = true,
            onCheckedChange = {},
        )
        switchSetting(
            label = "Allow Discord to track screen reader usage",
            checked = true,
            onCheckedChange = {},
        )
    }
}
