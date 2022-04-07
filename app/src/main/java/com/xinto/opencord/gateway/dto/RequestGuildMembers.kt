package com.xinto.opencord.gateway.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestGuildMembers(
    @SerialName("guildId")
    val guildId: List<Long>,

    @SerialName("query")
    val query: String = "",

    @SerialName("limit")
    val limit: Int = 0,

    @SerialName("presences")
    val presences: Boolean = false,

    @SerialName("userIds")
    val userIds: List<Long> = emptyList(),
)
