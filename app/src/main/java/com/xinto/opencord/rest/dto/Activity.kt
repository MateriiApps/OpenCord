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

//    @SerialName("party")
//    val party: Any? = null,

//    @SerialName("assets")
//    val assets: Any? = null,

//    @SerialName("secrets")
//    val secrets: Any? = null,

    @SerialName("instance")
    val instance: Boolean? = null,

    @SerialName("flags")
    val flags: Int? = null,

//    @SerialName("buttons")
//    val buttons: List<Any>? = null,
)

@Serializable
data class ApiActivityEmoji(
    val name: String,
    val id: ApiSnowflake? = null,
    val animated: Boolean? = null,
)

@Serializable
data class ApiActivityTimestamp(
    val start: Int?,
    val end: Int?,
)
