package com.xinto.opencord.rest.models.embed

import com.xinto.opencord.domain.embed.DomainEmbed
import com.xinto.opencord.rest.models.ApiColor
import com.xinto.opencord.rest.models.toApi
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

    @SerialName("image")
    val image: ApiEmbedMedia? = null,

    @SerialName("author")
    val author: ApiEmbedAuthor? = null,

    @SerialName("fields")
    val fields: List<ApiEmbedField>? = null
)

fun DomainEmbed.toApi(): ApiEmbed {
    return ApiEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.toApi(),
        timestamp = timestamp,
        author = author?.let { ApiEmbedAuthor(name = it) },
        image = image,
        fields = fields,
    )
}
