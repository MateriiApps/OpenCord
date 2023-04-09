package com.xinto.opencord.rest.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiEmbedProvider(
    @SerialName("name")
    val name: String? = null,

    @SerialName("url")
    val url: String? = null,
)
