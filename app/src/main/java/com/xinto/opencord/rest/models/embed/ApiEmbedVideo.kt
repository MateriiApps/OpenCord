package com.xinto.opencord.rest.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiEmbedVideo(
    @SerialName("url")
    val url: String,

    @SerialName("height")
    val height: Int? = null,

    @SerialName("width")
    val width: Int? = null,
)
