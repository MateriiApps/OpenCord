package com.xinto.opencord.domain.model

import com.xinto.opencord.util.Timestamp
import kotlinx.datetime.Instant

data class DomainMessage(
    val id: ULong,
    val channelId: ULong,
    val timestamp: Instant,
    val content: String,
    val author: DomainUser,
    val attachments: List<DomainAttachment>,
    val embeds: List<DomainEmbed>
) {
    val formattedTimestamp
        get() = Timestamp.getFormattedTimestamp(timestamp)
}
