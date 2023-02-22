package com.xinto.opencord.domain.emoji

import androidx.compose.runtime.Immutable
import com.github.materiiapps.partial.getOrNull
import com.xinto.opencord.rest.models.emoji.ApiEmoji

/**
 * A unique identifier of a [DomainEmoji] that is obtained through [DomainEmoji.identifier]
 */
@Immutable
@JvmInline
value class DomainEmojiIdentifier(private val hashCode: Int?) {
    val exists: Boolean
        get() = hashCode != null
}

@Immutable
sealed interface DomainEmoji {
    val identifier: DomainEmojiIdentifier
}

fun ApiEmoji.toDomain(): DomainEmoji {
    return when {
        id == null && name != null -> DomainUnicodeEmoji(
            emoji = name,
        )
        id != null -> DomainGuildEmoji(
            name = name,
            id = id.value,
            animated = animated.getOrNull() ?: false,
        )
        else -> DomainUnknownEmoji
    }
}
