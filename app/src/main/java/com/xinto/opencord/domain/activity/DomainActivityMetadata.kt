package com.xinto.opencord.domain.activity

import com.xinto.opencord.rest.dto.ApiActivityMetadata

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
