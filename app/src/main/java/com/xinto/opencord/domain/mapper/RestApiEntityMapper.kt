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
        type = type,
        timestamp = timestamp.toEpochMilliseconds(),
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

fun ApiChannel.toEntity(): EntityChannel {
    return EntityChannel(
        id = id.value,
        guildId = guildId?.value
            ?: throw Error("cannot convert an ApiChannel to EntityChannel without guild id"),
        name = name,
        type = type,
        position = position,
        parentId = parentId?.value,
        nsfw = nsfw,
        permissions = permissions.value,
    )
}

fun ApiGuild.toEntity(): EntityGuild {
    return EntityGuild(
        id = id.value,
        name = name,
        icon = icon,
        bannerUrl = banner,
        permissions = permissions.value,
        premiumTier = premiumTier,
        premiumSubscriptionCount = premiumSubscriptionCount,
    )
}
