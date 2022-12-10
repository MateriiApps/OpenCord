package com.xinto.opencord.domain.activity

import androidx.compose.runtime.Immutable
import com.xinto.opencord.rest.models.activity.ApiActivityMetadata

@Immutable
data class DomainActivityMetadata(
    val albumId: String?,
    val artistIds: List<String>?,
    val contextUri: String?,
)

fun ApiActivityMetadata.toDomain(): DomainActivityMetadata {
    return DomainActivityMetadata(
        albumId = albumId,
        artistIds = artistIds,
        contextUri = contextUri,
    )
}
