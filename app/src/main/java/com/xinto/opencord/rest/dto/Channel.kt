package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiChannel(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("guild_id")
    val guildId: ApiSnowflake? = null,

    @SerialName("name")
    val name: String,

    @SerialName("type")
    val type: Int,

    @SerialName("position")
    val position: Int = 0,

    @SerialName("parent_id")
    val parentId: ApiSnowflake? = null,

    @SerialName("nsfw")
    val nsfw: Boolean = false,

    @SerialName("permissions")
    val permissions: ApiPermissions = ApiPermissions(0)
)
