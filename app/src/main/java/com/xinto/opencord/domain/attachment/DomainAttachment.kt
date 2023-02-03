package com.xinto.opencord.domain.attachment

import androidx.compose.runtime.Immutable
import com.xinto.opencord.db.entity.message.EntityAttachment
import com.xinto.opencord.rest.models.ApiAttachment

@Immutable
interface DomainAttachment {
    val id: Long
    val filename: String
    val size: Int
    val url: String
    val proxyUrl: String
    val type: String?
}

fun ApiAttachment.toDomain(): DomainAttachment {
    return when {
        contentType?.startsWith("video/") == true -> DomainVideoAttachment(
            id = id.value,
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
            width = width ?: 100,
            height = height ?: 100,
            type = contentType,
        )
        contentType?.startsWith("image/") == true -> DomainPictureAttachment(
            id = id.value,
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
            width = width ?: 100,
            height = height ?: 100,
            type = contentType,
        )
        else -> DomainFileAttachment(
            id = id.value,
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
            type = contentType,
        )
    }
}

fun EntityAttachment.toDomain(): DomainAttachment {
    return if (contentType?.isNotEmpty() == true) {
        when (contentType) {
            // TODO: more explicit video and picture types
            "video/mp4" -> DomainVideoAttachment(
                id = id,
                filename = fileName,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100,
                type = contentType,
            )
            else -> DomainPictureAttachment(
                id = id,
                filename = fileName,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100,
                type = contentType,
            )
        }
    } else {
        DomainFileAttachment(
            id = id,
            filename = fileName,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
            type = contentType,
        )
    }
}
