package com.xinto.opencord.rest.models.emoji

import com.github.materiiapps.partial.Partial
import com.github.materiiapps.partial.missing
import com.github.materiiapps.partial.partial
import com.xinto.opencord.domain.emoji.DomainEmoji
import com.xinto.opencord.domain.emoji.DomainGuildEmoji
import com.xinto.opencord.domain.emoji.DomainUnicodeEmoji
import com.xinto.opencord.domain.emoji.DomainUnknownEmoji
import com.xinto.opencord.rest.models.ApiSnowflake
import kotlinx.serialization.Serializable

@Serializable
data class ApiEmoji(
    val name: String?,
    val id: ApiSnowflake?,
    val animated: Partial<Boolean> = missing(),
)

fun DomainEmoji.toApi(): ApiEmoji? {
    return when (this) {
        is DomainUnicodeEmoji -> ApiEmoji(
            name = emoji,
            id = null,
        )
        is DomainGuildEmoji -> ApiEmoji(
            name = name,
            id = ApiSnowflake(id),
            animated = if (animated) partial(true) else missing(),
        )
        is DomainUnknownEmoji -> null
    }
}
