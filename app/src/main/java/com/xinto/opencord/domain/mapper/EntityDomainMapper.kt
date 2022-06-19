package com.xinto.opencord.domain.mapper

import androidx.compose.ui.graphics.Color
import com.xinto.opencord.db.entity.*
import com.xinto.opencord.domain.model.*
import com.xinto.opencord.rest.service.DiscordCdnServiceImpl

fun EntityAttachment.toDomain(): DomainAttachment {
    return if (contentType.isNotEmpty()) {
        when (contentType) {
            "video/mp4" -> DomainAttachment.Video(
                id = id,
                filename = filename,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100
            )
            else -> DomainAttachment.Picture(
                id = id,
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
            id = id,
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
        )
    }
}

fun EntityMessageFull.toDomain(): DomainMessage {
    val domainId = message.id
    val domainChannelId = message.channelId
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
    val avatarUrl = avatar
        ?.let { DiscordCdnServiceImpl.getUserAvatarUrl(id.toString(), it) }
        ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(discriminator.toInt().rem(5))

    return when {
        locale != null -> DomainUserPrivate(
            id = id,
            username = username,
            discriminator = discriminator,
            avatarUrl = avatarUrl,
            bot = bot,
            bio = bio,
            flags = (publicFlags ?: 0) or (privateFlags ?: 0),
            pronouns = pronouns,
            mfaEnabled = mfaEnabled!!,
            verified = verified!!,
            email = email!!,
            phone = phone,
            locale = locale,
        )
        premium != null -> DomainUserReadyEvent(
            id = id,
            username = username,
            discriminator = discriminator,
            avatarUrl = avatarUrl,
            bot = bot,
            bio = bio,
            flags = privateFlags ?: 0,
            mfaEnabled = mfaEnabled!!,
            verified = verified!!,
            premium = premium,
            purchasedFlags = purchasedFlags!!,
        )
        else -> DomainUserPublic(
            id = id,
            username = username,
            discriminator = discriminator,
            avatarUrl = avatarUrl,
            bot = bot,
            bio = bio,
            flags = (publicFlags ?: 0) or (privateFlags ?: 0),
            pronouns = pronouns,
        )
    }
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
            id = id,
            guildId = guildId,
            name = name,
            position = position,
            parentId = parentId,
            permissions = permissions
        )
        4 -> DomainChannel.Category(
            id = id,
            guildId = guildId,
            name = name,
            position = position,
            permissions = permissions
        )
        5 -> DomainChannel.AnnouncementChannel(
            id = id,
            guildId = guildId,
            name = name,
            position = position,
            parentId = parentId,
            permissions = permissions,
            nsfw = nsfw
        )
        else -> DomainChannel.TextChannel(
            id = id,
            guildId = guildId,
            name = name,
            position = position,
            parentId = parentId,
            permissions = permissions,
            nsfw = nsfw
        )
    }
}