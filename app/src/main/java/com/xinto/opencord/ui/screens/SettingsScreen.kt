package com.xinto.opencord.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.xinto.opencord.R
import com.xinto.opencord.ui.navigation.AppDestination.Settings
import com.xinto.opencord.ui.screens.settings.accessibilityScreen
import com.xinto.opencord.ui.screens.settings.accountSettingsScreen
import com.xinto.opencord.ui.screens.settings.appearanceSettingsScreen
import com.xinto.opencord.ui.screens.settings.connectionsScreen
import com.xinto.opencord.ui.screens.settings.devicesScreen
import com.xinto.opencord.ui.screens.settings.friendRequestsSettingsScreen
import com.xinto.opencord.ui.screens.settings.languageScreen
import com.xinto.opencord.ui.screens.settings.notificationSettingsScreen
import com.xinto.opencord.ui.screens.settings.privacySafetySettingsScreen
import com.xinto.opencord.ui.screens.settings.userProfileScreen
import com.xinto.opencord.ui.screens.settings.voiceSettingsScreen
import com.xinto.opencord.util.ifNotNullComposable
import kotlinx.collections.immutable.persistentListOf

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onCategoryClick: (Settings) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null,
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            fun category(
                @DrawableRes icon: Int,
                @StringRes text: Int,
                onClick: () -> Unit
            ) {
                item {
                    ListItem(
                        modifier = Modifier.clickable(onClick = onClick),
                        leadingContent = {
                            Icon(
                                painter = painterResource(icon),
                                contentDescription = null,
                            )
                        },
                        headlineText = {
                            Text(stringResource(text))
                        },
                    )
                }
            }

            section("User Settings") {
                persistentListOf(
                    Settings.ACCOUNT,
                    Settings.USER_PROFILE,
                    Settings.PRIVACY_SAFETY,
                    Settings.DEVICES,
                    Settings.CONNECTIONS,
                    Settings.FRIEND_REQUESTS,
                ).forEach { category ->
                    category(
                        onClick = { onCategoryClick(category) },
                        icon = category.icon,
                        text = category.label,
                    )
                }
            }

            section("App Settings") {
                persistentListOf(
                    Settings.VOICE_VIDEO,
                    Settings.NOTIFICATIONS,
                    Settings.APPEARANCE,
                    Settings.ACCESSIBILITY,
                    Settings.LANGUAGE,
                ).forEach { category ->
                    category(
                        onClick = { onCategoryClick(category) },
                        icon = category.icon,
                        text = category.label,
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsCategory(
    dest: Settings,
    onClickBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = null,
                        )
                    }
                },
                title = {
                    Text(stringResource(dest.label))
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            when (dest) {
                Settings.ACCOUNT -> accountSettingsScreen()
                Settings.USER_PROFILE -> userProfileScreen()
                Settings.PRIVACY_SAFETY -> privacySafetySettingsScreen()
                Settings.DEVICES -> devicesScreen()
                Settings.CONNECTIONS -> connectionsScreen()
                Settings.FRIEND_REQUESTS -> friendRequestsSettingsScreen()
                Settings.VOICE_VIDEO -> voiceSettingsScreen()
                Settings.NOTIFICATIONS -> notificationSettingsScreen()
                Settings.APPEARANCE -> appearanceSettingsScreen()
                Settings.ACCESSIBILITY -> accessibilityScreen()
                Settings.LANGUAGE -> languageScreen()
            }
        }
    }
}

context(LazyListScope)
fun setting(
    label: String,
    description: String? = null,
    onClick: () -> Unit,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    item {
        ListItem(
            modifier = Modifier.clickable(onClick = onClick),
            headlineText = { Text(label) },
            supportingText = description.ifNotNullComposable { Text(it) },
            trailingContent = trailingContent,
        )
    }
}

context(LazyListScope)
fun switchSetting(
    label: String,
    description: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    setting(
        label = label,
        description = description,
        onClick = { onCheckedChange(!checked) },
    ) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

context(LazyListScope)
fun section(
    label: String,
    content: () -> Unit
) {
    item {
        Text(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 18.dp,
            ),
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
        )
    }

    content()

    item {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Divider(
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 8.dp,
                    )
                    .align(Alignment.Center),
            )
        }
    }
}
