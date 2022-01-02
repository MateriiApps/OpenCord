package com.xinto.opencord.network.gateway.data.outgoing

import com.google.gson.annotations.SerializedName

data class RequestGuildMembers(
    @SerializedName("guildId") val guildId: List<Long>,
    @SerializedName("query") val query: String = "",
    @SerializedName("limit") val limit: Int = 0,
    @SerializedName("presences") val presences: Boolean = false,
    @SerializedName("userIds") val userIds: List<Long> = emptyList(),
)
