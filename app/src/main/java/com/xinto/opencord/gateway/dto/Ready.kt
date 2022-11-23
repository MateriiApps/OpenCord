package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.dto.ApiGuild
import com.xinto.opencord.rest.dto.ApiUser
import com.xinto.opencord.rest.dto.ApiUserSettings
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
