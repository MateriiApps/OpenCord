package com.xinto.opencord.rest.models.activity

import com.xinto.opencord.domain.activity.DomainActivityAssets
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiActivityAssets(
    @SerialName("large_image")
    val largeImage: String? = null,

    @SerialName("large_text")
    val largeText: String? = null,

    @SerialName("small_image")
    val smallImage: String? = null,

    @SerialName("small_text")
    val smallText: String? = null,
)

fun DomainActivityAssets.toApi(): ApiActivityAssets {
    return ApiActivityAssets(
        largeImage = largeImage,
        largeText = largeText,
        smallImage = smallImage,
        smallText = smallText,
    )
}
