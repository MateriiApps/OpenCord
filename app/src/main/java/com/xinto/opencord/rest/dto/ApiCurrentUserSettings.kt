package com.xinto.opencord.rest.body

import com.xinto.opencord.rest.dto.ApiSnowflake
import com.xinto.partialgen.Partial
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// TODO: check what the commented out parts are
@Partial
@Serializable
data class ApiCurrentUserSettings(
    @SerialName("locale")
    val locale: String,

    @SerialName("show_current_game")
    val showCurrentGame: Boolean,

//    @SerialName("restricted_guilds")
//    val restrictedGuilds: List<ApiSnowflake>

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
    val theme: ApiThemeSetting,

    @SerialName("developer_mode")
    val developerMode: Boolean,

    @SerialName("guild_positions")
    val guildPositions: List<ULong>,

    @SerialName("detect_platform_accounts")
    val detectPlatformAccounts: Boolean,

    @SerialName("status")
    val status: ApiStatus,

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
    val animateStickers: Boolean,

//    @SerialName("friend_discovery_flags")
//    val friendDiscoveryFlags: Int,

    @SerialName("view_nsfw_guilds")
    val viewNsfwGuilds: Boolean,

    @SerialName("passwordless")
    val passwordless: Boolean,

    @SerialName("friend_source_flags")
    val friendSourceFlags: ApiFriendSources,

    @SerialName("guild_folders")
    val guildFolders: List<ApiGuildFolder>,

    @SerialName("custom_status")
    val customStatus: String?,
)

@Serializable
data class ApiFriendSources(
    @SerialName("all")
    val all: Boolean? = null,

    @SerialName("mutual_friends")
    val mutualFriends: Boolean? = null,

    @SerialName("mutual_guilds")
    val mutualGuilds: Boolean? = null,
)

@Serializable
data class ApiGuildFolder(
    @SerialName("guild_ids")
    val guildIds: List<ApiSnowflake>,

//    @SerialName("id")
//    val id: ApiSnowflake? = null,

//    @SerialName("name")
//    val name: String? = null,

//    @SerialName("color")
//    val color: Any? = null,
)

@Serializable(ApiThemeSetting.Serializer::class)
enum class ApiThemeSetting {
    DARK,
    LIGHT;

    companion object Serializer : KSerializer<ApiThemeSetting> {
        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("ThemeSetting", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): ApiThemeSetting {
            return when (val theme = decoder.decodeString()) {
                "dark" -> DARK
                "light" -> LIGHT
                else -> throw IllegalStateException("Unknown theme setting $theme")
            }
        }

        override fun serialize(encoder: Encoder, value: ApiThemeSetting) {
            val serialized = when (value) {
                DARK -> "dark"
                LIGHT -> "light"
            }
            encoder.encodeString(serialized)
        }
    }
}

@Serializable(ApiStatus.Serializer::class)
enum class ApiStatus {
    ONLINE,
    IDLE,
    DND,
    INVISIBLE;

    companion object Serializer : KSerializer<ApiStatus> {
        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("ApiStatus", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): ApiStatus {
            return when (val status = decoder.decodeString()) {
                "online" -> ONLINE
                "idle" -> IDLE
                "dnd" -> DND
                "invisible" -> INVISIBLE
                else -> throw IllegalStateException("Unknown status $status")
            }
        }

        override fun serialize(encoder: Encoder, value: ApiStatus) {
            val serialized = when (value) {
                ONLINE -> "online"
                IDLE -> "idle"
                DND -> "dnd"
                INVISIBLE -> "invisible"
            }
            encoder.encodeString(serialized)
        }
    }
}
