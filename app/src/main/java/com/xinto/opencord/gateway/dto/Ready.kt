package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.models.ApiGuild
import com.xinto.opencord.rest.models.user.ApiUser
import com.xinto.opencord.rest.models.user.settings.ApiUserSettings
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ready(
    @SerialName("user")
    val user: ApiUser,

    @SerialName("guilds")
    val guilds: List<ApiGuild>,

    @SerialName("sessions")
    val sessions: List<SessionData>,

    @SerialName("session_id")
    val sessionId: String,

    @SerialName("user_settings")
    val userSettings: ApiUserSettings,

    @SerialName("read_state")
    val readState: ReadState
)

@Serializable
data class ReadState(
    val version: Int,
    val partial: Boolean,
    val entries: List<ReadStateEntry>
)

@Serializable
data class ReadStateEntry(
    @SerialName("id")
    val channelId: Long,

    @SerialName("mention_count")
    val mentionCount: Int,

    @SerialName("last_pin_timestamp")
    val lastPinTimestamp: Instant,

    @SerialName("last_message_id")
    val lastMessageId: Long,
)
