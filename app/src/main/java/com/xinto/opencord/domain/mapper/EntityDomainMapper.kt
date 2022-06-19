package com.xinto.opencord.domain.mapper

import androidx.compose.ui.graphics.Color
import com.xinto.opencord.db.entity.*
import com.xinto.opencord.domain.model.*
import com.xinto.opencord.rest.service.DiscordCdnServiceImpl

fun EntityAttachment.toDomain(): DomainAttachment {
    return if (contentType.isNotEmpty()) {
        when (contentType) {
            "video/mp4" -> DomainAttachment.Video(
                id = id.toULong(),
                filename = filename,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100
            )
            else -> DomainAttachment.Picture(
                id = id.toULong(),
                filename = filename,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100
            )
        }
    } else {
        DomainAttachment.File(
            id = id.toULong(),
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
        )
    }
}

fun EntityMessageFull.toDomain(): DomainMessage {
    val domainId = message.id.toULong()
    val domainChannelId = message.channelId.toULong()
    val domainContent = message.content
    val domainAuthor = message.author.toDomain()
    val domainTimestamp = message.timestamp
    val domainEditedTimestamp = message.editedTimestamp
    return when (message.type) {
        EntityMessageType.Default, EntityMessageType.Reply -> {
            val domainAttachments = attachments.map { it.toDomain() }
//            val domainEmbeds = embeds.map { it.toDomain() }
//            val domainReferencedMessage = referencedMessage?.toDomain()
            DomainMessageRegular(
                id = domainId,
                channelId = domainChannelId,
                content = domainContent,
                author = domainAuthor,
                timestamp = domainTimestamp,
                editedTimestamp = domainEditedTimestamp,
                attachments = domainAttachments,
                embeds = emptyList()/*domainEmbeds*/,
                isReply = false /*type == EntityMessageType.Reply*/,
                referencedMessage = null /*domainReferencedMessage as? DomainMessageRegular*/
            )
        }
        EntityMessageType.GuildMemberJoin -> {
            DomainMessageMemberJoin(
                id = domainId,
                channelId = domainChannelId,
                content = domainContent,
                timestamp = domainTimestamp,
                author = domainAuthor
            )
        }
    }
}
fun EntityUser.toDomain(): DomainUser {
    val avatarUrl = avatar?.let { avatar ->
        DiscordCdnServiceImpl.getUserAvatarUrl(id.toString(), avatar)
    } ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(discriminator.toInt().rem(5))
    return DomainUser(
        id = id.toULong(),
        username = username,
        discriminator = discriminator,
        avatarUrl = avatarUrl,
        bot = bot,
    )
}

fun EntityEmbed.toDomain(): DomainEmbed {
//    val domainFields = fields?.map { it.toDomain() }
    val domainColor = color?.let {
        Color(it)
    }
//    val domainAuthor = author?.toDomain()
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = domainColor,
        author = null /*domainAuthor*/,
        fields = null /*domainFields*/
    )
}

fun EntityEmbedAuthor.toDomain(): DomainEmbedAuthor {
    return DomainEmbedAuthor(
        name = name
    )
}

fun EntityEmbedField.toDomain(): DomainEmbedField {
    return DomainEmbedField(
        name = name,
        value = value,
    )
}

fun EntityChannel.toDomain(): DomainChannel {
    val permissions = DomainPermission.values().filter {
        (permissions and it.flags) == it.flags
    }
    return when (type) {
        2 -> DomainChannel.VoiceChannel(
            id = id.toULong(),
            guildId = guildId?.toULong(),
            name = name,
            position = position,
            parentId = parentId?.toULong(),
            permissions = permissions
        )
        4 -> DomainChannel.Category(
            id = id.toULong(),
            guildId = guildId?.toULong(),
            name = name,
            position = position,
            permissions = permissions
        )
        5 -> DomainChannel.AnnouncementChannel(
            id = id.toULong(),
            guildId = guildId?.toULong(),
            name = name,
            position = position,
            parentId = parentId?.toULong(),
            permissions = permissions,
            nsfw = nsfw
        )
        else -> DomainChannel.TextChannel(
            id = id.toULong(),
            guildId = guildId?.toULong(),
            name = name,
            position = position,
            parentId = parentId?.toULong(),
            permissions = permissions,
            nsfw = nsfw
        )
    }
}