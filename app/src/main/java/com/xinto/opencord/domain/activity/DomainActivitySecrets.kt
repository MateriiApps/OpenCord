package com.xinto.opencord.domain.activity

import com.xinto.opencord.rest.dto.ApiActivitySecrets

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
