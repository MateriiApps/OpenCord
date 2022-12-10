package com.xinto.opencord.domain.activity

import androidx.compose.runtime.Immutable
import com.xinto.opencord.rest.dto.ApiActivityAssets

@Immutable
data class DomainActivityAssets(
    val largeImage: String?,
    val largeText: String?,
    val smallImage: String?,
    val smallText: String?,
)

fun ApiActivityAssets.toDomain(): DomainActivityAssets {
    return DomainActivityAssets(
        largeImage = largeImage,
        largeText = largeText,
        smallImage = smallImage,
        smallText = smallText,
    )
}
