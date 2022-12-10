package com.xinto.opencord.rest.models.activity

import com.xinto.opencord.domain.activity.DomainActivitySecrets
import kotlinx.serialization.Serializable

@Serializable
data class ApiActivitySecrets(
    val join: String? = null,
    val spectate: String? = null,
    val match: String? = null,
)

fun DomainActivitySecrets.toApi(): ApiActivitySecrets {
    return ApiActivitySecrets(
        join = join,
        spectate = spectate,
        match = match,
    )
}
