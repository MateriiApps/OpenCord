package com.xinto.opencord.domain.usersettings

import androidx.compose.runtime.Immutable
import com.github.materiiapps.partial.Partialize
import com.github.materiiapps.partial.map
import com.xinto.opencord.rest.models.user.settings.ApiUserSettings
import com.xinto.opencord.rest.models.user.settings.ApiUserSettingsPartial

@Immutable
@Partialize
data class DomainUserSettings(
    val locale: String,
    val showCurrentGame: Boolean,
//    val restrictedGuilds: List<Long>
//    val defaultGuildsRestricted: Boolean,
    val inlineAttachmentMedia: Boolean,
    val inlineEmbedMedia: Boolean,
    val gifAutoPlay: Boolean,
    val renderEmbeds: Boolean,
    val renderReactions: Boolean,
    val animateEmoji: Boolean,
    val enableTTSCommand: Boolean,
    val messageDisplayCompact: Boolean,
    val convertEmoticons: Boolean,
//    val explicitContentFilter: Int,
    val disableGamesTab: Boolean,
    val theme: DomainThemeSetting,
    val developerMode: Boolean,
    val detectPlatformAccounts: Boolean,
    val status: DomainUserStatus,
    val afkTimeout: Int,
    val timezoneOffset: Int,
    val streamNotificationsEnabled: Boolean,
    val allowAccessibilityDetection: Boolean,
    val contactSyncEnabled: Boolean,
    val nativePhoneIntegrationEnabled: Boolean,
    val animateStickers: Int, // int??
    val friendDiscoveryFlags: Int, // int??
    val viewNsfwGuilds: Boolean,
    val passwordless: Boolean,
    val friendSourceFlags: DomainFriendSources,
    val guildFolders: List<DomainGuildFolder>,
    val customStatus: DomainCustomStatus?,
)

fun ApiUserSettings.toDomain(): DomainUserSettings {
    val domainTheme = DomainThemeSetting.fromValue(theme)
        ?: throw IllegalArgumentException("Invalid theme $theme")
    val domainStatus = DomainUserStatus.fromValue(status)
        ?: throw IllegalArgumentException("Invalid status $status")

    return DomainUserSettings(
        locale = locale,
        showCurrentGame = showCurrentGame,
        inlineAttachmentMedia = inlineAttachmentMedia,
        inlineEmbedMedia = inlineEmbedMedia,
        gifAutoPlay = gifAutoPlay,
        renderEmbeds = renderEmbeds,
        renderReactions = renderReactions,
        animateEmoji = animateEmoji,
        enableTTSCommand = enableTTSCommand,
        messageDisplayCompact = messageDisplayCompact,
        convertEmoticons = convertEmoticons,
        disableGamesTab = disableGamesTab,
        theme = domainTheme,
        developerMode = developerMode,
        detectPlatformAccounts = detectPlatformAccounts,
        status = domainStatus,
        afkTimeout = afkTimeout,
        timezoneOffset = timezoneOffset,
        streamNotificationsEnabled = streamNotificationsEnabled,
        allowAccessibilityDetection = allowAccessibilityDetection,
        contactSyncEnabled = contactSyncEnabled,
        nativePhoneIntegrationEnabled = nativePhoneIntegrationEnabled,
        animateStickers = animateStickers,
        friendDiscoveryFlags = friendDiscoveryFlags,
        viewNsfwGuilds = viewNsfwGuilds,
        passwordless = passwordless,
        friendSourceFlags = friendSourceFlags.toDomain(),
        guildFolders = guildFolders.map { it.toDomain() },
        customStatus = customStatus?.toDomain(),
    )
}

fun ApiUserSettingsPartial.toDomain(): DomainUserSettingsPartial {
    return DomainUserSettingsPartial(
        locale = locale,
        showCurrentGame = showCurrentGame,
        inlineAttachmentMedia = inlineAttachmentMedia,
        inlineEmbedMedia = inlineEmbedMedia,
        gifAutoPlay = gifAutoPlay,
        renderEmbeds = renderEmbeds,
        renderReactions = renderReactions,
        animateEmoji = animateEmoji,
        enableTTSCommand = enableTTSCommand,
        messageDisplayCompact = messageDisplayCompact,
        convertEmoticons = convertEmoticons,
        disableGamesTab = disableGamesTab,
        theme = theme.map { DomainThemeSetting.fromValue(it)!! },
        developerMode = developerMode,
        detectPlatformAccounts = detectPlatformAccounts,
        status = status.map { DomainUserStatus.fromValue(it)!! },
        afkTimeout = afkTimeout,
        timezoneOffset = timezoneOffset,
        streamNotificationsEnabled = streamNotificationsEnabled,
        allowAccessibilityDetection = allowAccessibilityDetection,
        contactSyncEnabled = contactSyncEnabled,
        nativePhoneIntegrationEnabled = nativePhoneIntegrationEnabled,
        animateStickers = animateStickers,
        friendDiscoveryFlags = friendDiscoveryFlags,
        viewNsfwGuilds = viewNsfwGuilds,
        passwordless = passwordless,
        friendSourceFlags = friendSourceFlags.map { it.toDomain() },
        guildFolders = guildFolders.map { guildFolders ->
            guildFolders.map { it.toDomain() }
        },
        customStatus = customStatus.map { it?.toDomain() },
    )
}
