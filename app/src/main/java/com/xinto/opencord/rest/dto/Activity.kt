package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiActivity(
    @SerialName("name")
    val name: String,

    @SerialName("type")
    val type: Int,

    @SerialName("url")
    val url: String? = null,

    @SerialName("created_at")
    val createdAt: Long? = null,

    @SerialName("timestamps")
    val timestamps: ApiActivityTimestamp? = null,

    @SerialName("application_id")
    val applicationId: ApiSnowflake? = null,

    @SerialName("details")
    val details: String? = null,

    @SerialName("state")
    val state: String? = null,

    @SerialName("emoji")
    val emoji: ApiActivityEmoji? = null,

    @SerialName("party")
    val party: ApiActivityParty? = null,

    @SerialName("assets")
    val assets: ApiActivityAssets? = null,

    @SerialName("secrets")
    val secrets: ApiActivitySecrets? = null,

    @SerialName("instance")
    val instance: Boolean? = null,

    @SerialName("flags")
    val flags: Int? = null,

    // TODO: verify whether its List<String name> or List<ApiActivityButton> since I've seen both
//    @SerialName("buttons")
//    val buttons: List<ApiActivityButton>? = null,

    @SerialName("id")
    val id: String? = null,

    @SerialName("metadata")
    val metadata: ApiActivityMetadata? = null,

    @SerialName("sync_id")
    val syncId: String? = null,
)

// TODO: replace this with ApiEmoji when reactions pr is merged
@Serializable
data class ApiActivityEmoji(
    val name: String? = null,
    val id: ApiSnowflake? = null,
    val animated: Boolean? = null,
)

@Serializable
data class ApiActivityTimestamp(
    val start: String? = null,
    val end: String? = null,
)

@Serializable
data class ApiActivityParty(
    val id: String? = null,
    val size: List<Int>? = null,
)

@Serializable
data class ApiActivityAssets(
    @SerialName("large_image")
    val largeImage: String? = null,

    @SerialName("large_text")
    val largeText: String? = null,

    @SerialName("small_image")
    val smallImage: String? = null,

    @SerialName("small_text")
    val smallText: String? = null,
)

@Serializable
data class ApiActivitySecrets(
    val join: String? = null,
    val spectate: String? = null,
    val match: String? = null,
)

//@Serializable
//data class ApiActivityButton(
//    val label: String,
//    val url: String,
//)

@Serializable
data class ApiActivityMetadata(
    @SerialName("album_id")
    val albumId: String? = null,

    @SerialName("artist_ids")
    val artistIds: List<String>? = null,

    @SerialName("context_uri")
    val contextUri: String? = null,
)
