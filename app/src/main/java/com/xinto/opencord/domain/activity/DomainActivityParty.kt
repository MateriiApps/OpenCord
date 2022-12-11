package com.xinto.opencord.domain.activity

import androidx.compose.runtime.Immutable
import com.xinto.opencord.rest.models.activity.ApiActivityParty

@Immutable
data class DomainActivityParty(
    val id: String?,
    val currentSize: Int?,
    val maxSize: Int?,
)

fun ApiActivityParty.toDomain(): DomainActivityParty {
    return DomainActivityParty(
        id = id,
        currentSize = size?.get(0),
        maxSize = size?.get(1),
    )
}
