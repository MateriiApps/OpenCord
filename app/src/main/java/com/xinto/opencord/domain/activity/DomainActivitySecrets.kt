package com.xinto.opencord.domain.activity

import androidx.compose.runtime.Immutable
import com.xinto.opencord.rest.models.activity.ApiActivitySecrets

@Immutable
data class DomainActivitySecrets(
    val join: String?,
    val spectate: String?,
    val match: String?,
)

fun ApiActivitySecrets.toDomain(): DomainActivitySecrets {
    return DomainActivitySecrets(
        join = join,
        spectate = spectate,
        match = match,
    )
}
