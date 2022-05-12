package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.dto.ApiEmojiPartial
import com.xinto.opencord.rest.dto.ApiGuildMember
import com.xinto.opencord.rest.dto.ApiSnowflake
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

    @SerialName("member")
    val member: ApiGuildMember? = null,

    @SerialName("emoji")
    val emoji: ApiEmojiPartial
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
    val emoji: ApiEmojiPartial
)