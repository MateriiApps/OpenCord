package com.xinto.opencord.rest.dto

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiEmbed(
    @SerialName("title")
    val title: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("url")
    val url: String? = null,

    @SerialName("color")
    val color: ApiColor? = null,

    @SerialName("timestamp")
    val timestamp: Instant? = null,

    @SerialName("author")
    val author: ApiEmbedAuthor? = null,

    @SerialName("fields")
    val fields: List<ApiEmbedField>? = null
)

@Serializable
data class ApiEmbedAuthor(
    @SerialName("name")
    val name: String
)

@Serializable
data class ApiEmbedField(
    @SerialName("name")
    val name: String,

    @SerialName("value")
    val value: String,
)