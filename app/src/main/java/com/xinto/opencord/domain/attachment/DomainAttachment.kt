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
}

fun ApiAttachment.toDomain(): DomainAttachment {
    return when {
        contentType.startsWith("video/") -> DomainVideoAttachment(
            id = id.value,
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
            width = width ?: 100,
            height = height ?: 100,
        )
        contentType.startsWith("image/") -> DomainPictureAttachment(
            id = id.value,
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
            width = width ?: 100,
            height = height ?: 100,
        )
        else -> DomainFileAttachment(
            id = id.value,
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
        )
    }
}

fun EntityAttachment.toDomain(): DomainAttachment {
    return if (contentType?.isNotEmpty() == true) {
        when (contentType) {
            "video/mp4" -> DomainVideoAttachment(
                id = id,
                filename = fileName,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100,
            )
            else -> DomainPictureAttachment(
                id = id,
                filename = fileName,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100,
            )
        }
    } else {
        DomainFileAttachment(
            id = id,
            filename = fileName,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
        )
    }
}
