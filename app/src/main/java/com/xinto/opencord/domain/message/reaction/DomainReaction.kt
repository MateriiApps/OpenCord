package com.xinto.opencord.domain.message.reaction

import androidx.compose.runtime.Immutable
import com.xinto.opencord.db.entity.reactions.EntityReaction
import com.xinto.opencord.domain.emoji.*
import com.xinto.opencord.rest.models.reaction.ApiReaction

@Immutable
data class DomainReaction(
    val count: Int,
    val meReacted: Boolean,
    val emoji: DomainEmoji,
)

fun ApiReaction.toDomain(): DomainReaction {
    return DomainReaction(
        count = count,
        meReacted = meReacted,
        emoji = emoji.toDomain(),
    )
}

fun EntityReaction.toDomain(): DomainReaction {
    val emoji = when {
        emojiId > 0L -> DomainGuildEmoji(
            id = emojiId,
            name = emojiName,
            animated = animated,
        )
        emojiName.isNotEmpty() -> DomainUnicodeEmoji(
            emoji = emojiName,
        )
        else -> DomainUnknownEmoji
    }

    return DomainReaction(
        count = count,
        meReacted = meReacted,
        emoji = emoji,
    )
}
