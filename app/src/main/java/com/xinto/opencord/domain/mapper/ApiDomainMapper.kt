package com.xinto.opencord.domain.mapper

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.model.*
import com.xinto.opencord.rest.dto.*

fun ApiAttachment.toDomain(): DomainAttachment {
    return if (contentType.isNotEmpty()) {
        when (contentType) {
            "video/mp4" -> DomainAttachment.Video(
                id = id,
                filename = filename,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 0,
                height = height ?: 0
            )
            else -> DomainAttachment.Picture(
                id = id,
                filename = filename,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 0,
                height = height ?: 0
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
    return DomainGuild(
        id = id.toLong(),
        name = name,
        iconUrl = icon?.let { "${BuildConfig.URL_CDN}/icons/$id/$icon" },
        bannerUrl = banner?.let { "${BuildConfig.URL_CDN}/banners/$id/$banner" },
    )
}

fun ApiGuildMember.toDomain(): DomainGuildMember {
    return DomainGuildMember(
        user = user?.let { DomainUser.fromApi(it) },
        nick = nick,
        avatarUrl = avatar?.let { "${BuildConfig.URL_CDN}/avatars/${user?.id}/$avatar.png" }
            ?: "${BuildConfig.URL_CDN}/embed/avatars/${user?.discriminator?.toInt()?.rem(5)}.png"
    )
}

fun ApiGuildMemberChunk.toDomain(): DomainGuildMemberChunk {
    return DomainGuildMemberChunk(
        guildId = guildId,
        guildMembers = members.map { it.toDomain() },
        chunkIndex = chunkIndex,
        chunkCount = chunkCount,
    )
}

fun ApiMeGuild.toDomain(): DomainMeGuild {
    return DomainMeGuild(
        id = id.toLong(),
        name = name,
        iconUrl = "https://cdn.discordapp.com/icons/$id/$icon",
        permissions = permissions.toLong()
    )
}

fun ApiMessage.toDomain(): DomainMessage {
    return DomainMessage(
        id = id,
        content = content,
        channelId = channelId,
        author = DomainUser.fromApi(author),
        attachments = attachments.map { it.toDomain() },
        embeds = listOf()
    )
}

fun ApiUser.toDomain(): DomainUser {
    return DomainUser(
        id = id,
        username = username,
        discriminator = discriminator,
        avatarUrl = avatar?.let { "${BuildConfig.URL_CDN}/avatars/$id/$avatar.png" }
            ?: "${BuildConfig.URL_CDN}/embed/avatars/${discriminator.toInt() % 5}.png",
        bot = bot,
    )
}