package com.xinto.opencord.rest.models.activity

import com.xinto.opencord.domain.activity.DomainActivityTimestamp
import kotlinx.serialization.Serializable

@Serializable
data class ApiActivityTimestamp(
    val start: String? = null,
    val end: String? = null,
)

fun DomainActivityTimestamp.toApi(): ApiActivityTimestamp {
    return ApiActivityTimestamp(
        start = start?.toEpochMilliseconds().toString(),
        end = end?.toEpochMilliseconds().toString(),
    )
}
