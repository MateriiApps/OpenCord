package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.dto.ApiSnowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDeleteData(
    @SerialName("id")
    val messageId: ApiSnowflake,

    @SerialName("channel_id")
    val channelId: ApiSnowflake,

    @SerialName("guild_id")
    val guildId: ApiSnowflake,
)
