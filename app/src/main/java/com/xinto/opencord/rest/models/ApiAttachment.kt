package com.xinto.opencord.rest.models

import com.xinto.opencord.domain.attachment.DomainAttachment
import com.xinto.opencord.domain.attachment.DomainPictureAttachment
import com.xinto.opencord.domain.attachment.DomainVideoAttachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiAttachment(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("filename")
    val filename: String,

    @SerialName("size")
    val size: Int,

    @SerialName("url")
    val url: String,

    @SerialName("proxy_url")
    val proxyUrl: String,

    @SerialName("width")
    val width: Int? = null,

    @SerialName("height")
    val height: Int? = null,

    @SerialName("content_type")
    val contentType: String? = null
)

fun DomainAttachment.toApi(): ApiAttachment {
    return when (this) {
        is DomainVideoAttachment -> ApiAttachment(
            id = ApiSnowflake(id),
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
            width = width,
            height = height,
            contentType = type,
        )
        is DomainPictureAttachment -> ApiAttachment(
            id = ApiSnowflake(id),
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
            width = width,
            height = height,
            contentType = type,
        )
        else -> ApiAttachment(
            id = ApiSnowflake(id),
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
            width = null,
            height = null,
            contentType = type,
        )
    }
}
