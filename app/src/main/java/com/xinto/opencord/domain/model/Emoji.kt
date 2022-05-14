package com.xinto.opencord.domain.model

sealed interface DomainEmoji {
    val name: String?
}

data class DomainEmojiCustom(
    override val name: String?,
    val id: ULong,
    val url: String
): DomainEmoji

data class DomainEmojiUnicode(
    override val name: String,
): DomainEmoji
