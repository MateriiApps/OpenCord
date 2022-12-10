package com.xinto.opencord.rest.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiEmbedAuthor(
    @SerialName("name")
    val name: String
)
