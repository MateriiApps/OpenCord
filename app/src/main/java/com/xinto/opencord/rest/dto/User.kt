package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiUser(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("username")
    val username: String,

    @SerialName("discriminator")
    val discriminator: String,

    @SerialName("avatar")
    val avatar: String?,

    @SerialName("bot")
    val bot: Boolean = false,
)