package com.xinto.opencord.gateway.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageAckData(
    @SerialName("ack_type")
    val ackType: Int? = null,

    @SerialName("channel_id")
    val channelId: Long,

    @SerialName("message_id")
    val messageId: Long,

    @SerialName("version")
    val version: Int,

    @SerialName("mention_count")
    val mentionCount: Int? = 0,
)
