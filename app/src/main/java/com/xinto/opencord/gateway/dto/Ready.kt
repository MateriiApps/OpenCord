package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.models.ApiGuild
import com.xinto.opencord.rest.models.user.ApiUser
import com.xinto.opencord.rest.models.user.settings.ApiUserSettings
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
)
