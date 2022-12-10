package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.models.ApiSnowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GuildDeleteData(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("unavailable")
    val unavailable: Boolean,
)
