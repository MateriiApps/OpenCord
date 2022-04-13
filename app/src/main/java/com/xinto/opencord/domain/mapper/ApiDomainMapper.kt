package com.xinto.opencord.domain.mapper

import com.xinto.opencord.domain.model.*
import com.xinto.opencord.rest.dto.*
import com.xinto.opencord.rest.service.DiscordCdnServiceImpl

fun ApiAttachment.toDomain(): DomainAttachment {
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

fun ApiChannel.toDomain(): DomainChannel {
    return when (type) {
        2 -> DomainChannel.VoiceChannel(
            id = id.toLong(),
            name = name,
            position = position,
            parentId = parentId
        )
        4 -> DomainChannel.Category(
            id = id.toLong(),
            name = name,
            position = position,
        )
        5 -> DomainChannel.AnnouncementChannel(
            id = id.toLong(),
            name = name,
            position = position,
            parentId = parentId,
            nsfw = nsfw
        )
        else -> DomainChannel.TextChannel(
            id = id.toLong(),
            name = name,
            position = position,
            parentId = parentId,
            nsfw = nsfw
        )
    }
}

fun ApiGuild.toDomain(): DomainGuild {
    val iconUrl = icon?.let { icon ->
        DiscordCdnServiceImpl.getGuildIconUrl(id.toLong(), icon)
    }
    val bannerUrl = banner?.let { banner ->
        DiscordCdnServiceImpl.getGuildBannerUrl(id.toLong(), banner)
    }
    return DomainGuild(
        id = id.toLong(),
        name = name,
        iconUrl = iconUrl,
        bannerUrl = bannerUrl,
    )
}

fun ApiGuildMember.toDomain(): DomainGuildMember {
    val avatarUrl = user?.let { user ->
        avatar?.let { avatar ->
            DiscordCdnServiceImpl.getUserAvatarUrl(user.id, avatar)
        } ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(user.discriminator.toInt().rem(5))
    }
    val domainUser = user?.let { DomainUser.fromApi(it) }
    return DomainGuildMember(
        user = domainUser,
        nick = nick,
        avatarUrl = avatarUrl
    )
}

fun ApiGuildMemberChunk.toDomain(): DomainGuildMemberChunk {
    val domainMembers = members.map { it.toDomain() }
    return DomainGuildMemberChunk(
        guildId = guildId,
        guildMembers = domainMembers,
        chunkIndex = chunkIndex,
        chunkCount = chunkCount,
    )
}

fun ApiMeGuild.toDomain(): DomainMeGuild {
    val iconUrl = icon?.let { icon ->
        DiscordCdnServiceImpl.getGuildIconUrl(id.toLong(), icon)
    }
    val iconText = name.split("""\s+""".toRegex()).map { it[0] }.joinToString()
    return DomainMeGuild(
        id = id.toLong(),
        name = name,
        iconUrl = iconUrl,
        iconText = iconText,
        permissions = permissions.toLong()
    )
}

fun ApiMessage.toDomain(): DomainMessage {
    val domainAuthor = DomainUser.fromApi(author)
    val domainAttachments = attachments.map { it.toDomain() }
    return DomainMessage(
        id = id,
        content = content,
        channelId = channelId,
        author = domainAuthor,
        attachments = domainAttachments,
        embeds = listOf()
    )
}

fun ApiUser.toDomain(): DomainUser {
    val avatarUrl = avatar?.let { avatar ->
        DiscordCdnServiceImpl.getUserAvatarUrl(id, avatar)
    } ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(discriminator.toInt().rem(5))
    return DomainUser(
        id = id,
        username = username,
        discriminator = discriminator,
        avatarUrl = avatarUrl,
        bot = bot,
    )
}