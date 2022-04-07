package com.xinto.opencord.domain.model

data class DomainMessage(
    val id: Long,
    val channelId: Long,
    val content: String,
    val author: DomainUser,
    val attachments: List<DomainAttachment>,
    val embeds: List<DomainEmbed>
)
