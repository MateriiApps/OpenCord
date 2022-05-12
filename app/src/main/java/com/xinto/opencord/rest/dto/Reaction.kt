package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiReaction(
    @SerialName("count")
    val count: Int,

    @SerialName("me")
    val meReacted: Boolean,

    @SerialName("emoji")
    val emoji: ApiEmojiPartial,
)