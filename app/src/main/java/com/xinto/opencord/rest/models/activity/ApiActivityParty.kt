package com.xinto.opencord.rest.models.activity

import com.xinto.opencord.domain.activity.DomainActivityParty
import kotlinx.serialization.Serializable

@Serializable
data class ApiActivityParty(
    val id: String? = null,
    val size: List<Int>? = null,
)

fun DomainActivityParty.toApi(): ApiActivityParty {
    return ApiActivityParty(
        id = id,
        size = if (currentSize == null || maxSize == null) null else {
            listOf(currentSize, maxSize)
        },
    )
}
