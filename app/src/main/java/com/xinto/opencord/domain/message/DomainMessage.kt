package com.xinto.opencord.domain.message

import androidx.compose.runtime.Immutable
import com.github.materiiapps.partial.*
import com.xinto.opencord.db.entity.message.EntityMessage
import com.xinto.opencord.domain.attachment.DomainAttachment
import com.xinto.opencord.domain.attachment.toDomain
import com.xinto.opencord.domain.embed.DomainEmbed
import com.xinto.opencord.domain.embed.toDomain
import com.xinto.opencord.domain.user.DomainUser
import com.xinto.opencord.domain.user.toDomain
import com.xinto.opencord.rest.models.message.ApiMessage
import com.xinto.opencord.rest.models.message.ApiMessagePartial
import com.xinto.opencord.rest.models.message.ApiMessageType
import com.xinto.opencord.rest.models.message.fromValue
import com.xinto.simpleast.Node
import kotlinx.datetime.Instant

@Immutable
@Partialize(
    children = [
        DomainMessageRegular::class,
        DomainMessageMemberJoin::class,
        DomainMessageUnknown::class,
    ],
)
interface DomainMessage {
    @Required
    val id: Long

    @Required
    val channelId: Long

    val timestamp: Instant
    val pinned: Boolean
    val content: String
    val author: DomainUser

    @Skip
    val contentNodes: List<Node<Any?>>

    @Skip
    val formattedTimestamp: String

    @Skip
    val isDeletable: Boolean
}

fun ApiMessage.toDomain(): DomainMessage {
    return when (type) {
        ApiMessageType.Default, ApiMessageType.Reply -> {
            DomainMessageRegular(
                id = id.value,
                channelId = channelId.value,
                content = content,
                author = author.toDomain(),
                timestamp = timestamp,
                pinned = pinned,
                editedTimestamp = editedTimestamp,
                attachments = attachments.map { it.toDomain() },
                embeds = embeds.map { it.toDomain() },
                isReply = type == ApiMessageType.Reply,
                referencedMessage = referencedMessage?.toDomain(),
                mentionEveryone = mentionEveryone,
                mentions = mentions.map { it.toDomain() },
            )
        }
        ApiMessageType.GuildMemberJoin -> {
            DomainMessageMemberJoin(
                id = id.value,
                content = content,
                channelId = channelId.value,
                timestamp = timestamp,
                pinned = pinned,
                author = author.toDomain(),
            )
        }
        ApiMessageType.Unknown -> DomainMessageUnknown(
            id = id.value,
            content = content,
            channelId = channelId.value,
            timestamp = timestamp,
            pinned = pinned,
            author = author.toDomain(),
        )
    }
}

fun ApiMessagePartial.toDomain(): DomainMessagePartial {
    return when (val type = type.getOrNull()) {
        ApiMessageType.Default, ApiMessageType.Reply -> {
            DomainMessageRegularPartial(
                id = id.value,
                channelId = channelId.value,
                content = content,
                author = author.map { it.toDomain() },
                timestamp = timestamp,
                pinned = pinned,
                editedTimestamp = editedTimestamp,
                attachments = attachments.map { it.map { it.toDomain() } },
                embeds = embeds.map { it.map { it.toDomain() } },
                isReply = partial(type == ApiMessageType.Reply),
                referencedMessage = referencedMessage.map { it?.toDomain() },
                mentionEveryone = mentionEveryone,
                mentions = mentions.map { it.map { it.toDomain() } },
            )
        }
        ApiMessageType.GuildMemberJoin -> {
            DomainMessageMemberJoinPartial(
                id = id.value,
                channelId = channelId.value,
                content = content,
                author = author.map { it.toDomain() },
                timestamp = timestamp,
                pinned = pinned,
            )
        }
        ApiMessageType.Unknown, null -> DomainMessageUnknownPartial(
            id = id.value,
            channelId = channelId.value,
            content = content,
            author = author.map { it.toDomain() },
            timestamp = timestamp,
            pinned = pinned,
        )
    }
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
                referencedMessage = referencedMessage,
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
        ApiMessageType.Unknown, null -> DomainMessageUnknown(
            id = id,
            content = content,
            channelId = channelId,
            timestamp = Instant.fromEpochMilliseconds(timestamp),
            pinned = pinned,
            author = author,
        )
    }
}
