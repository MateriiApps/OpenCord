package com.xinto.opencord.domain.model

import com.xinto.opencord.util.SimpleAstParser
import com.xinto.opencord.util.Timestamp
import com.xinto.partialgen.Partialable
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

sealed class DomainMessage {
    abstract val id: Long
    abstract val channelId: Long
    abstract val timestamp: Instant
    abstract val content: String
    abstract val author: DomainUser

    val formattedTimestamp by lazy { Timestamp.getFormattedTimestamp(timestamp) }
}

@Partialable
data class DomainMessageRegular(
    override val id: Long,
    override val channelId: Long,
    override val timestamp: Instant,
    override val content: String,
    override val author: DomainUser,
    val editedTimestamp: Instant?,
    val attachments: List<DomainAttachment>,
    val embeds: List<DomainEmbed>,
    val isReply: Boolean,
    val referencedMessage: DomainMessageRegular?
): DomainMessage(), KoinComponent {
    private val parser: SimpleAstParser = get()

    val isEdited = editedTimestamp != null
    val contentNodes = parser.parse(content, null)
}

data class DomainMessageMemberJoin(
    override val id: Long,
    override val channelId: Long,
    override val timestamp: Instant,
    override val content: String,
    override val author: DomainUser
): DomainMessage()