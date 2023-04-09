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

    @SerialName("footer")
    val footer: ApiEmbedFooter? = null,

    @SerialName("thumbnail")
    val thumbnail: ApiEmbedMedia? = null,

    @SerialName("image")
    val image: ApiEmbedMedia? = null,

    @SerialName("video")
    val video: ApiEmbedMedia? = null,

    @SerialName("author")
    val author: ApiEmbedAuthor? = null,

    @SerialName("provider")
    val provider: ApiEmbedProvider? = null,

    @SerialName("fields")
    val fields: List<ApiEmbedField>? = null,
)

fun DomainEmbed.toApi(): ApiEmbed {
    return ApiEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.toApi(),
        timestamp = footer?.timestamp,
        author = author,
        footer = footer?.toApi(),
        thumbnail = thumbnail,
        image = image,
        video = video,
        provider = provider,
        fields = fields,
    )
}
