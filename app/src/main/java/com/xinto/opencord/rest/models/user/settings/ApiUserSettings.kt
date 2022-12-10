package com.xinto.opencord.rest.models.user.settings

import com.github.materiiapps.partial.Partialize
import com.github.materiiapps.partial.map
import com.xinto.opencord.domain.usersettings.DomainUserSettingsPartial
import com.xinto.opencord.rest.models.ApiSnowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Partialize
@Serializable
data class ApiUserSettings(
    @SerialName("locale")
    val locale: String,

    @SerialName("show_current_game")
    val showCurrentGame: Boolean,

    // servers with dms turned off
    @SerialName("restricted_guilds")
    val restrictedGuilds: List<ApiSnowflake>,

    // turn off dms from servers automatically upon joining
    @SerialName("default_guilds_restricted")
    val defaultGuildsRestricted: Boolean,

    @SerialName("inline_attachment_media")
    val inlineAttachmentMedia: Boolean,

    @SerialName("inline_embed_media")
    val inlineEmbedMedia: Boolean,

    @SerialName("gif_auto_play")
    val gifAutoPlay: Boolean,

    @SerialName("render_embeds")
    val renderEmbeds: Boolean,

    @SerialName("render_reactions")
    val renderReactions: Boolean,

    @SerialName("animate_emoji")
    val animateEmoji: Boolean,

    @SerialName("enable_tts_command")
    val enableTTSCommand: Boolean,

    @SerialName("message_display_compact")
    val messageDisplayCompact: Boolean,

    @SerialName("convert_emoticons")
    val convertEmoticons: Boolean,

    // This appears to be edited through the protobuf settings, so not sure what role this plays here
//    @SerialName("explicit_content_filter")
//    val explicitContentFilter: Int,

    @SerialName("disable_games_tab")
    val disableGamesTab: Boolean,

    @SerialName("theme")
    val theme: String,

    @SerialName("developer_mode")
    val developerMode: Boolean,

    @SerialName("detect_platform_accounts")
    val detectPlatformAccounts: Boolean,

    @SerialName("status")
    val status: String,

    @SerialName("afk_timeout")
    val afkTimeout: Int,

    @SerialName("timezone_offset")
    val timezoneOffset: Int,

    @SerialName("stream_notifications_enabled")
    val streamNotificationsEnabled: Boolean,

    @SerialName("allow_accessibility_detection")
    val allowAccessibilityDetection: Boolean,

    @SerialName("contact_sync_enabled")
    val contactSyncEnabled: Boolean,

    @SerialName("native_phone_integration_enabled")
    val nativePhoneIntegrationEnabled: Boolean,

    @SerialName("animate_stickers")
    val animateStickers: Int,

    @SerialName("friend_discovery_flags")
    val friendDiscoveryFlags: Int,

    @SerialName("view_nsfw_guilds")
    val viewNsfwGuilds: Boolean,

    @SerialName("passwordless")
    val passwordless: Boolean,

    @SerialName("friend_source_flags")
    val friendSourceFlags: ApiFriendSources,

    @SerialName("guild_folders")
    val guildFolders: List<ApiGuildFolder>,

    @SerialName("custom_status")
    val customStatus: ApiCustomStatus?,
)

fun DomainUserSettingsPartial.toApi(): ApiUserSettingsPartial {
    return ApiUserSettingsPartial(
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
        theme = theme.map { it.value },
        developerMode = developerMode,
        detectPlatformAccounts = detectPlatformAccounts,
        status = status.map { it.value },
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
        friendSourceFlags = friendSourceFlags.map { it.toApi() },
        guildFolders = guildFolders.map { guildFolders ->
            guildFolders.map { it.toApi() }
        },
        customStatus = customStatus.map { it?.toApi() },
    )
}
