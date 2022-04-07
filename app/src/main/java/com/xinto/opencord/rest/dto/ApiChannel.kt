package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiChannel(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("type")
    val type: Int,

    @SerialName("position")
    val position: Int = 0,

    @SerialName("parent_id")
    val parentId: Long? = null,

    @SerialName("nsfw")
    val nsfw: Boolean = false,
)
