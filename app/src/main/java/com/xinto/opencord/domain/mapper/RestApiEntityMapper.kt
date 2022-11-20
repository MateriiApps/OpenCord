package com.xinto.opencord.domain.mapper

import com.xinto.opencord.db.entity.message.EntityAttachment
import com.xinto.opencord.db.entity.message.EntityEmbed
import com.xinto.opencord.db.entity.message.EntityMessage
import com.xinto.opencord.db.entity.message.EntityUser
import com.xinto.opencord.rest.dto.ApiAttachment
import com.xinto.opencord.rest.dto.ApiEmbed
import com.xinto.opencord.rest.dto.ApiMessage
import com.xinto.opencord.rest.dto.ApiUser

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
