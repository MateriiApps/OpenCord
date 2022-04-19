package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.dto.ApiGuild
import com.xinto.opencord.rest.dto.ApiUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ready(
    @SerialName("user")
    val user: ApiUser,

    @SerialName("session_id")
    val sessionId: String,

    @SerialName("guilds")
    val guilds: List<ApiGuild>
)
