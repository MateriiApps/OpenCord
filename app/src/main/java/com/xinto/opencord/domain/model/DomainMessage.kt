package com.xinto.opencord.domain.model

data class DomainMessage(
    val id: ULong,
    val channelId: ULong,
    val content: String,
    val author: DomainUser,
    val attachments: List<DomainAttachment>,
    val embeds: List<DomainEmbed>
)
