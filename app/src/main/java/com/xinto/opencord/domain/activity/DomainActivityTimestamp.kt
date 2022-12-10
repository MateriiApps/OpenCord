package com.xinto.opencord.domain.activity

import androidx.compose.runtime.Immutable
import com.xinto.opencord.rest.dto.ApiActivityTimestamp
import kotlinx.datetime.Instant

@Immutable
data class DomainActivityTimestamp(
    val start: Instant?,
    val end: Instant?,
)

fun ApiActivityTimestamp.toDomain(): DomainActivityTimestamp {
    return DomainActivityTimestamp(
        start = start?.let { Instant.fromEpochMilliseconds(it.toLong()) },
        end = end?.let { Instant.fromEpochMilliseconds(it.toLong()) },
    )
}
