package com.xinto.opencord.gateway.dto

import com.xinto.opencord.rest.models.activity.ApiActivity
import kotlinx.serialization.Serializable

@Serializable
data class UpdatePresence(
    val since: Long,
    val status: String,
    val afk: Boolean?,
    val activities: List<ApiActivity>,
)
