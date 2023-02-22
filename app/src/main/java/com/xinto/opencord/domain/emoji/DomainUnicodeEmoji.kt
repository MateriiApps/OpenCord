package com.xinto.opencord.domain.emoji

import androidx.compose.runtime.Immutable

@Immutable
data class DomainUnicodeEmoji(
    val emoji: String,
) : DomainEmoji {
    override val identifier: DomainEmojiIdentifier
        get() = DomainEmojiIdentifier(emoji.hashCode())
}
