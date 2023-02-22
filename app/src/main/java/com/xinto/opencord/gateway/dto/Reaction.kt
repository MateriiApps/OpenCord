package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.models.ApiSnowflake
import com.xinto.opencord.rest.models.emoji.ApiEmoji
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageReactionAddData(
    @SerialName("user_id")
    val userId: ApiSnowflake,

    @SerialName("channel_id")
    val channelId: ApiSnowflake,

    @SerialName("message_id")
    val messageId: ApiSnowflake,

    @SerialName("guild_id")
    val guildId: ApiSnowflake? = null,

    @SerialName("emoji")
    val emoji: ApiEmoji,

//    @SerialName("member")
//    val member: ApiGuildMember? = null,
)

@Serializable
data class MessageReactionRemoveData(
    @SerialName("user_id")
    val userId: ApiSnowflake,

    @SerialName("channel_id")
    val channelId: ApiSnowflake,

    @SerialName("message_id")
    val messageId: ApiSnowflake,

    @SerialName("guild_id")
    val guildId: ApiSnowflake? = null,

    @SerialName("emoji")
    val emoji: ApiEmoji,
)

@Serializable
data class MessageReactionRemoveAllData(
    @SerialName("message_id")
    val messageId: ApiSnowflake,

    @SerialName("channel_id")
    val channelId: ApiSnowflake,

    @SerialName("guild_id")
    val guildId: ApiSnowflake? = null,
)
