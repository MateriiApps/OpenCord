package com.xinto.opencord.domain.mapper

import androidx.compose.ui.graphics.Color
import com.xinto.opencord.domain.model.*
import com.xinto.opencord.rest.dto.*
import com.xinto.opencord.rest.service.DiscordCdnServiceImpl

fun ApiAttachment.toDomain(): DomainAttachment {
    return if (contentType.isNotEmpty()) {
        when (contentType) {
            "video/mp4" -> DomainAttachment.Video(
                id = id.value,
                filename = filename,
                size = size,
                url = url,
                proxyUrl = proxyUrl,
                width = width ?: 100,
                height = height ?: 100
            )
            else -> DomainAttachment.Picture(
                id = id.value,
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
            id = id.value,
            filename = filename,
            size = size,
            url = url,
            proxyUrl = proxyUrl,
        )
    }
}

fun ApiChannel.toDomain(): DomainChannel {
    val permissions = permissions.toDomain()
    return when (type) {
        2 -> DomainChannel.VoiceChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            permissions = permissions
        )
        4 -> DomainChannel.Category(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            permissions = permissions
        )
        5 -> DomainChannel.AnnouncementChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            permissions = permissions,
            nsfw = nsfw
        )
        else -> DomainChannel.TextChannel(
            id = id.value,
            guildId = guildId?.value,
            name = name,
            position = position,
            parentId = parentId?.value,
            permissions = permissions,
            nsfw = nsfw
        )
    }
}

fun ApiGuild.toDomain(): DomainGuild {
    val iconUrl = icon?.let { icon ->
        DiscordCdnServiceImpl.getGuildIconUrl(id.toString(), icon)
    }
    val bannerUrl = banner?.let { banner ->
        DiscordCdnServiceImpl.getGuildBannerUrl(id.toString(), banner)
    }
    return DomainGuild(
        id = id.value,
        name = name,
        iconUrl = iconUrl,
        bannerUrl = bannerUrl,
        permissions = permissions.toDomain(),
        premiumTier = premiumTier,
        premiumSubscriptionCount = premiumSubscriptionCount ?: 0,
    )
}

fun ApiGuildMember.toDomain(): DomainGuildMember {
    val avatarUrl = user?.let { user ->
        avatar?.let { avatar ->
            DiscordCdnServiceImpl.getUserAvatarUrl(user.id.toString(), avatar)
        } ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(user.discriminator.toInt().rem(5))
    }
    val domainUser = user?.toDomain()
    return DomainGuildMember(
        user = domainUser,
        nick = nick,
        avatarUrl = avatarUrl
    )
}

fun ApiGuildMemberChunk.toDomain(): DomainGuildMemberChunk {
    val domainMembers = members.map { it.toDomain() }
    return DomainGuildMemberChunk(
        guildId = guildId.value,
        guildMembers = domainMembers,
        chunkIndex = chunkIndex,
        chunkCount = chunkCount,
    )
}

fun ApiMeGuild.toDomain(): DomainMeGuild {
    val iconUrl = icon?.let { icon ->
        DiscordCdnServiceImpl.getGuildIconUrl(id.toString(), icon)
    }
    return DomainMeGuild(
        id = id.value,
        name = name,
        iconUrl = iconUrl,
        permissions = permissions.toDomain()
    )
}

fun ApiMessage.toDomain(): DomainMessage {
    val domainAuthor = author.toDomain()
    return when (type) {
        ApiMessageType.Default -> {
            val domainAttachments = attachments.map { it.toDomain() }
            val domainEmbeds = embeds.map { it.toDomain() }
            DomainMessageRegular(
                id = id.value,
                channelId = channelId.value,
                content = content,
                author = domainAuthor,
                timestamp = timestamp,
                editedTimestamp = editedTimestamp,
                attachments = domainAttachments,
                embeds = domainEmbeds
            )
        }
        ApiMessageType.GuildMemberJoin -> {
            DomainMessageMemberJoin(
                id = id.value,
                content = content,
                channelId = channelId.value,
                timestamp = timestamp,
                author = domainAuthor
            )
        }
    }
}

fun ApiMessagePartial.toDomain(): DomainMessageRegularPartial {
    val domainAuthor = author?.toDomain()
    val domainAttachments = attachments?.map { it.toDomain() }
    val domainEmbeds = embeds?.map { it.toDomain() }
    return DomainMessageRegularPartial(
        id = id?.value,
        content = content,
        channelId = channelId?.value,
        author = domainAuthor,
        timestamp = timestamp,
        editedTimestamp = editedTimestamp,
        attachments = domainAttachments,
        embeds = domainEmbeds
    )
}

fun ApiUser.toDomain(): DomainUser {
    val avatarUrl = avatar?.let { avatar ->
        DiscordCdnServiceImpl.getUserAvatarUrl(id.toString(), avatar)
    } ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(discriminator.toInt().rem(5))
    return DomainUser(
        id = id.value,
        username = username,
        discriminator = discriminator,
        avatarUrl = avatarUrl,
        bot = bot,
    )
}

fun ApiPermissions.toDomain(): List<DomainPermission> {
    val permissions = value
    return DomainPermission.values().filter {
        (permissions and it.flags) == it.flags
    }
}

fun ApiEmbed.toDomain(): DomainEmbed {
    val domainFields = fields?.map { it.toDomain() }
    val domainColor = color?.let {
        Color(red = it.red, green = it.green, blue = it.blue)
    }
    val domainAuthor = author?.toDomain()
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = domainColor,
        author = domainAuthor,
        fields = domainFields
    )
}

fun ApiEmbedAuthor.toDomain(): DomainEmbedAuthor {
    return DomainEmbedAuthor(
        name = name
    )
}

fun ApiEmbedField.toDomain(): DomainEmbedField {
    return DomainEmbedField(
        name = name,
        value = value,
    )
}