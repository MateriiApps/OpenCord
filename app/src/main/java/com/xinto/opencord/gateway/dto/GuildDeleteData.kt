package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.dto.ApiSnowflake
import kotlinx.serialization.Serializable

@Serializable
data class GuildDeleteData(
    val id: ApiSnowflake,
    val unavailable: Boolean,
)
