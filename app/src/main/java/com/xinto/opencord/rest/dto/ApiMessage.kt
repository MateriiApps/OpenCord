package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiMessage(
    @SerialName("id")
    val id: Long,

    @SerialName("channel_id")
    val channelId: Long,

    @SerialName("content")
    val content: String,

    @SerialName("author")
    val author: ApiUser,

    @SerialName("attachments")
    val attachments: List<ApiAttachment>,
)