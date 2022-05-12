package com.xinto.opencord.domain.model

data class DomainReaction(
    val count: Int,
    val meReacted: Boolean,
    val emoji: DomainEmoji,
)