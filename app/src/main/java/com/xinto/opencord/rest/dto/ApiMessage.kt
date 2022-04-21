package com.xinto.opencord.rest.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiMessage(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("channel_id")
    val channelId: ApiSnowflake,

    @SerialName("timestamp")
    val timestamp: Instant,

    @SerialName("edited_timestamp")
    val editedTimestamp: Instant?,

    @SerialName("content")
    val content: String,

    @SerialName("author")
    val author: ApiUser,

    @SerialName("attachments")
    val attachments: List<ApiAttachment>,

    @SerialName("embeds")
    val embeds: List<ApiEmbed>
)

@Serializable
data class ApiMessagePartial(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("channel_id")
    val channelId: ApiSnowflake,

    @SerialName("timestamp")
    val timestamp: Instant? = null,

    @SerialName("edited_timestamp")
    val editedTimestamp: Instant? = null,

    @SerialName("content")
    val content: String? = null,

    @SerialName("author")
    val author: ApiUser? = null,

    @SerialName("attachments")
    val attachments: List<ApiAttachment>? = null,

    @SerialName("embeds")
    val embeds: List<ApiEmbed>? = null
)