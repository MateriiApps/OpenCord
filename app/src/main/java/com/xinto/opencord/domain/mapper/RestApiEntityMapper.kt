package com.xinto.opencord.domain.mapper

import com.xinto.opencord.db.entity.channel.EntityChannel
import com.xinto.opencord.db.entity.guild.EntityGuild
import com.xinto.opencord.db.entity.message.EntityAttachment
import com.xinto.opencord.db.entity.message.EntityEmbed
import com.xinto.opencord.db.entity.message.EntityMessage
import com.xinto.opencord.db.entity.user.EntityUser
import com.xinto.opencord.rest.dto.*

fun ApiMessage.toEntity(): EntityMessage {
    return EntityMessage(
        id = id.value,
        channelId = channelId.value,
        type = type.value,
        timestamp = timestamp.toEpochMilliseconds(),
        pinned = pinned,
        content = content,
        authorId = author.id.value,
        editedTimestamp = editedTimestamp?.toEpochMilliseconds(),
        referencedMessageId = referencedMessage?.id?.value,
        mentionsEveryone = mentionEveryone,
        hasAttachments = attachments.isNotEmpty(),
        hasEmbeds = embeds.isNotEmpty(),
    )
}

fun ApiAttachment.toEntity(messageId: Long): EntityAttachment {
    return EntityAttachment(
        id = id.value,
        messageId = messageId,
        fileName = filename,
        size = size,
        url = url,
        proxyUrl = proxyUrl,
        width = width,
        height = height,
        contentType = contentType,
    )
}

fun ApiEmbed.toEntity(messageId: Long, embedIndex: Int): EntityEmbed {
    return EntityEmbed(
        embedIndex = embedIndex,
        messageId = messageId,
        title = title,
        description = description,
        url = url,
        color = color?.rgbColor,
        timestamp = timestamp?.toEpochMilliseconds(),
        authorName = author?.name,
        fields = fields,
    )
}

fun ApiUser.toEntity(): EntityUser {
    return EntityUser(
        id = id.value,
        username = username,
        discriminator = discriminator,
        avatarHash = avatar,
        bot = bot,
        pronouns = pronouns,
        bio = bio,
        bannerUrl = banner,
        publicFlags = publicFlags ?: 0,
    )
}

fun ApiChannel.toEntity(guildId: Long): EntityChannel {
    return EntityChannel(
        id = id.value,
        guildId = this.guildId?.value ?: guildId,
        name = name,
        type = type,
        position = position,
        parentId = parentId?.value,
        nsfw = nsfw,
        pinsStored = false,
    )
}

fun ApiGuild.toEntity(): EntityGuild {
    return EntityGuild(
        id = id.value,
        name = name,
        icon = icon,
        banner = banner,
        premiumTier = premiumTier,
        premiumSubscriptionCount = premiumSubscriptionCount,
    )
}
