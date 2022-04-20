package com.xinto.opencord.domain.model

import com.xinto.opencord.util.SimpleAstParser
import com.xinto.opencord.util.Timestamp
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

data class DomainMessage(
    val id: ULong,
    val channelId: ULong,
    val timestamp: Instant,
    val editedTimestamp: Instant?,
    val content: String,
    val author: DomainUser,
    val attachments: List<DomainAttachment>,
    val embeds: List<DomainEmbed>
): KoinComponent {
    private val parser: SimpleAstParser = get()

    val formattedTimestamp = Timestamp.getFormattedTimestamp(timestamp)
    val isEdited = editedTimestamp != null
    val contentNodes = parser.parse(content, null)
}
