package com.xinto.opencord.domain.activity

import androidx.compose.runtime.Immutable
import com.xinto.opencord.rest.dto.ApiActivityEmoji

// TODO: use a partial emoji instead
@Immutable
data class DomainActivityEmoji(
    val name: String?,
    val id: Long?,
    val animated: Boolean?,
)

fun ApiActivityEmoji.toDomain(): DomainActivityEmoji {
    return DomainActivityEmoji(
        name = name,
        id = id?.value,
        animated = animated,
    )
}
