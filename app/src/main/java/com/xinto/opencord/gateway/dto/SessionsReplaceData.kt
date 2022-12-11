package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.models.activity.ApiActivity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias ReplaceSessionsData = List<SessionData>

@Serializable
data class SessionData(
    @SerialName("session_id")
    val sessionId: String,

    @SerialName("client_info")
    val clientInfo: ClientInfo,

    @SerialName("status")
    val status: String,

    @SerialName("activities")
    val activities: List<ApiActivity>,
)

@Serializable
data class ClientInfo(
    val client: String,
    val os: String,
)
