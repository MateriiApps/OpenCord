package com.xinto.opencord.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import com.xinto.opencord.ui.screens.settings.accountScreen
import com.xinto.opencord.ui.screens.settings.appearanceScreen
import com.xinto.opencord.ui.screens.settings.connectionsScreen
import com.xinto.opencord.ui.screens.settings.devicesScreen
import com.xinto.opencord.ui.screens.settings.friendRequestsScreen
import com.xinto.opencord.ui.screens.settings.languageScreen
import com.xinto.opencord.ui.screens.settings.notificationScreen
import com.xinto.opencord.ui.screens.settings.privacySafetyScreen
import com.xinto.opencord.ui.screens.settings.userProfileScreen
import com.xinto.opencord.ui.screens.settings.voiceScreen
import com.xinto.opencord.ui.screens.settings.webBrowserScreen
import com.xinto.opencord.util.ifNotNullComposable
import kotlinx.collections.immutable.ImmutableList
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
            fun section(label: String, categories: ImmutableList<Settings>) {
                section(label) {
                    items(categories) { category ->
                        ListItem(
                            modifier = Modifier.clickable { onCategoryClick(category) },
                            leadingContent = {
                                Icon(
                                    painter = painterResource(category.icon),
                                    contentDescription = null,
                                )
                            },
                            headlineText = {
                                Text(stringResource(category.label))
                            },
                        )
                    }
                }
            }

            section(
                label = "User Settings",
                categories = persistentListOf(
                    Settings.ACCOUNT,
                    Settings.USER_PROFILE,
                    Settings.PRIVACY_SAFETY,
                    Settings.DEVICES,
                    Settings.CONNECTIONS,
                    Settings.FRIEND_REQUESTS,
                ),
            )

            section(
                label = "App Settings",
                categories = persistentListOf(
                    Settings.APPEARANCE,
                    Settings.VOICE_VIDEO,
                    Settings.NOTIFICATIONS,
                    Settings.WEB_BROWSER,
                    Settings.ACCESSIBILITY,
                    Settings.LANGUAGE,
                ),
            )

            item {
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FilledTonalButton(
                        onClick = { /*TODO*/ },
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_logout),
                            contentDescription = null,
                        )

                        Spacer(Modifier.width(ButtonDefaults.IconSpacing))

                        Text(stringResource(R.string.logout))
                    }
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
                Settings.ACCOUNT -> accountScreen()
                Settings.USER_PROFILE -> userProfileScreen()
                Settings.PRIVACY_SAFETY -> privacySafetyScreen()
                Settings.DEVICES -> devicesScreen()
                Settings.CONNECTIONS -> connectionsScreen()
                Settings.FRIEND_REQUESTS -> friendRequestsScreen()
                Settings.VOICE_VIDEO -> voiceScreen()
                Settings.WEB_BROWSER -> webBrowserScreen()
                Settings.NOTIFICATIONS -> notificationScreen()
                Settings.APPEARANCE -> appearanceScreen()
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

    // divider()
}

context(LazyListScope)
fun divider() {
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

context(LazyListScope)
fun sliderSetting(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChange: (Float) -> Unit,
    @DrawableRes leadingIcon: Int,
    @DrawableRes trailingIcon: Int,
) {
    item {
        Row(
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(leadingIcon),
                contentDescription = null,
            )

            Slider(
                modifier = Modifier.weight(1f, true),
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                steps = steps,
            )

            Icon(
                painter = painterResource(trailingIcon),
                contentDescription = null,
            )
        }
    }
}

interface RadioSetting {
    val label: String
    val description: String?
}

context(LazyListScope)
inline fun <reified T> radioSection(
    label: String,
    value: T,
    crossinline onOptionClick: (T) -> Unit,
) where T : Enum<T>, T : RadioSetting {
    section(label) {
        items(enumValues<T>()) { option ->
            ListItem(
                modifier = Modifier.clickable {
                    onOptionClick(option)
                },
                headlineText = {
                    Text(option.label)
                },
                supportingText = option.description?.let { { Text(it) } },
                trailingContent = {
                    RadioButton(
                        selected = value == option,
                        onClick = null,
                    )
                },
            )
        }
    }
}
