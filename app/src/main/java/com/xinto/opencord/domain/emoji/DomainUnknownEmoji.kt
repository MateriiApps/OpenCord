package com.xinto.opencord.domain.emoji

import kotlinx.serialization.Serializable

@Serializable
object DomainUnknownEmoji : DomainEmoji {
    override val identifier: DomainEmojiIdentifier
        get() = DomainEmojiIdentifier(null)
}
