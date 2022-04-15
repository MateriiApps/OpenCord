package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.dto.ApiUser
import kotlinx.serialization.Serializable

@Serializable
data class Ready(
    val user: ApiUser
)
