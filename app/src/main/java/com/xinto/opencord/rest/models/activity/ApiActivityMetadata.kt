package com.xinto.opencord.rest.models.activity

import com.xinto.opencord.domain.activity.DomainActivityMetadata
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiActivityMetadata(
    @SerialName("album_id")
    val albumId: String? = null,

    @SerialName("artist_ids")
    val artistIds: List<String>? = null,

    @SerialName("context_uri")
    val contextUri: String? = null,
)

fun DomainActivityMetadata.toApi(): ApiActivityMetadata {
    return ApiActivityMetadata(
        albumId = albumId,
        artistIds = artistIds,
        contextUri = contextUri,
    )
}
