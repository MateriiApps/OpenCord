package com.xinto.opencord.rest.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiEmbedField(
    @SerialName("name")
    val name: String,

    @SerialName("value")
    val value: String,
)
