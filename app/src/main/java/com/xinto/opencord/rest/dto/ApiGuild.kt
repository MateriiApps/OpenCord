package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiGuild(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("icon")
    val icon: String? = null,

    @SerialName("banner")
    val banner: String? = null,
)