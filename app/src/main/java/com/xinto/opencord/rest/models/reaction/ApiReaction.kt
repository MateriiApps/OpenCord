package com.xinto.opencord.rest.models.reaction

import com.xinto.opencord.rest.models.emoji.ApiEmoji
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiReaction(
    @SerialName("emoji")
    val emoji: ApiEmoji,

    @SerialName("count")
    val count: Int,

    @SerialName("me")
    val meReacted: Boolean,
)
