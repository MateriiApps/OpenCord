package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.dto.ApiActivity
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePresence(
    val since: Int?,
    val activities: List<ApiActivity>,
    val status: String,
    val afk: Boolean,
)
