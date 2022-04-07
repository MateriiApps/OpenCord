package com.xinto.opencord.rest.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageBody(
    @SerialName("content")
    val content: String,
)
