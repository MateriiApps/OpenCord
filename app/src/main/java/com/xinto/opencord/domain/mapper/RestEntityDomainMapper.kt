package com.xinto.opencord.domain.mapper

import androidx.compose.ui.graphics.Color
import com.xinto.opencord.db.entity.message.EntityAttachment
import com.xinto.opencord.db.entity.message.EntityEmbed
import com.xinto.opencord.db.entity.message.EntityMessage
import com.xinto.opencord.db.entity.message.EntityUser
import com.xinto.opencord.domain.model.*
import com.xinto.opencord.rest.dto.ApiMessageType
import com.xinto.opencord.rest.service.DiscordCdnServiceImpl
import kotlinx.datetime.Instant

fun EntityMessage.toDomain(
    author: DomainUser,
    referencedMessage: DomainMessage?,
    embeds: List<DomainEmbed>?,
    attachments: List<DomainAttachment>?,
): DomainMessage {
    return when (type) {
        ApiMessageType.Default, ApiMessageType.Reply -> {
            DomainMessageRegular(
                id = id,
                channelId = channelId,
                content = content,
                author = author,
                timestamp = Instant.fromEpochMilliseconds(timestamp),
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
                author = author,
            )
        }
    }
}

fun EntityAttachment.toDomain(): DomainAttachment {
    return if (contentType?.isNotEmpty() == true) {
        when (contentType) {
            "video/mp4" -> DomainAttachment.Video(
                id = id,
                filename = fileName,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100
            )
            else -> DomainAttachment.Picture(
                id = id,
                filename = fileName,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100
            )
        }
    } else {
        DomainAttachment.File(
            id = id,
            filename = fileName,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
        )
    }
}

fun EntityEmbed.toDomain(): DomainEmbed {
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.let { Color(it) },
        author = authorName?.let { DomainEmbedAuthor(it) },
        fields = fields?.map { it.toDomain() }
    )
}

fun EntityUser.toDomain(): DomainUser {
    val avatarUrl = avatarHash
        ?.let { DiscordCdnServiceImpl.getUserAvatarUrl(id.toString(), it) }
        ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(discriminator.toInt().rem(5))

    return DomainUserPublic(
        id = id,
        username = username,
        discriminator = discriminator,
        avatarUrl = avatarUrl,
        bot = bot,
        bio = bio,
        flags = publicFlags,
        pronouns = pronouns,
    )
}
