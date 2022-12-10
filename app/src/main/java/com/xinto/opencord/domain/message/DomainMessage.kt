package com.xinto.opencord.domain.message

import com.xinto.opencord.db.entity.message.EntityMessage
import com.xinto.opencord.domain.attachment.DomainAttachment
import com.xinto.opencord.domain.embed.DomainEmbed
import com.xinto.opencord.domain.user.DomainUser
import com.xinto.opencord.rest.dto.ApiMessageType
import com.xinto.opencord.rest.dto.fromValue
import com.xinto.opencord.util.Timestamp
import kotlinx.datetime.Instant

abstract class DomainMessage {
    abstract val id: Long
    abstract val channelId: Long
    abstract val timestamp: Instant
    abstract val pinned: Boolean
    abstract val content: String
    abstract val author: DomainUser

    val formattedTimestamp by lazy { Timestamp.getFormattedTimestamp(timestamp) }
}

fun EntityMessage.toDomain(
    author: DomainUser,
    referencedMessage: DomainMessage?,
    embeds: List<DomainEmbed>?,
    attachments: List<DomainAttachment>?,
): DomainMessage {
    return when (val type = ApiMessageType.fromValue(type)) {
        ApiMessageType.Default, ApiMessageType.Reply -> {
            DomainMessageRegular(
                id = id,
                channelId = channelId,
                content = content,
                author = author,
                timestamp = Instant.fromEpochMilliseconds(timestamp),
                pinned = pinned,
                editedTimestamp = editedTimestamp?.let { Instant.fromEpochMilliseconds(it) },
                attachments = attachments ?: emptyList(),
                embeds = embeds ?: emptyList(),
                isReply = type == ApiMessageType.Reply,
                referencedMessage = referencedMessage as? DomainMessageRegular,
                mentionEveryone = mentionsEveryone,
//                mentions = mentions.map { it.toDomain() },
                mentions = emptyList(),
            )
        }
        ApiMessageType.GuildMemberJoin -> {
            DomainMessageMemberJoin(
                id = id,
                content = content,
                channelId = channelId,
                timestamp = Instant.fromEpochMilliseconds(timestamp),
                pinned = pinned,
                author = author,
            )
        }
        else -> DomainMessageUnknown(
            id = id,
            content = content,
            channelId = channelId,
            timestamp = Instant.fromEpochMilliseconds(timestamp),
            pinned = pinned,
            author = author,
        )
    }
}
