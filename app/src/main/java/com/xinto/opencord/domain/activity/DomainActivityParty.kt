package com.xinto.opencord.domain.activity

import com.xinto.opencord.rest.dto.ApiActivityParty

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
