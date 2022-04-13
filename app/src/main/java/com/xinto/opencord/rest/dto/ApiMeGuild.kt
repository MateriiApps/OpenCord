package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiMeGuild(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("name")
    val name: String,

    @SerialName("icon")
    val icon: String? = null,

    @SerialName("permissions")
    val permissions: String,
)