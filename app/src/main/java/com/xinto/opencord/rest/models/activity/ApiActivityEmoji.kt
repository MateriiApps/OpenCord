package com.xinto.opencord.rest.models.activity

import com.xinto.opencord.domain.activity.DomainActivityEmoji
import com.xinto.opencord.rest.models.ApiSnowflake
import kotlinx.serialization.Serializable

// TODO: replace this with ApiEmoji when reactions pr is merged
@Serializable
data class ApiActivityEmoji(
    val name: String? = null,
    val id: ApiSnowflake? = null,
    val animated: Boolean? = null,
)

fun DomainActivityEmoji.toApi(): ApiActivityEmoji {
    return ApiActivityEmoji(
        name = name,
        id = id?.let { ApiSnowflake(it) },
        animated = animated,
    )
}
