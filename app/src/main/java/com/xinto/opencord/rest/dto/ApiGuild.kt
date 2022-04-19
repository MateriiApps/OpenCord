package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiGuild(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("name")
    val name: String,

    @SerialName("icon")
    val icon: String?,

    @SerialName("banner")
    val banner: String? = null,

    @SerialName("permissions")
    val permissions: ApiPermissions = ApiPermissions(0)
)