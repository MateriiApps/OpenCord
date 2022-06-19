package com.xinto.opencord.domain.mapper

import com.xinto.opencord.db.entity.*
import com.xinto.opencord.rest.dto.*

fun ApiMessage.toEntity(): EntityMessageFull {
    val entityId = id.value
    val entityChannelId = channelId.value
    val entityAuthor = author.toEntity()
    val entityAttachments = attachments.map { it.toEntity() }
    val entityEmbeds = embeds.map { it.toEntity() }
    val entityType = type.toEntity()
//    val entityReferencedMessage = referencedMessage?.toEntity()
    val entityMessage = EntityMessage(
        id = entityId,
        channelId = entityChannelId,
        timestamp = timestamp,
        editedTimestamp = editedTimestamp,
        content = content,
        type = entityType,
        author = entityAuthor,
    )
    return EntityMessageFull(
        message = entityMessage,

        attachments = entityAttachments,
//        embeds = entityEmbeds,
    )
}

fun ApiUser.toEntity(): EntityUser {
    return EntityUser(
        id = id.value,
        username = username,
        discriminator = discriminator,
        avatar = avatar,
        bot = bot,
        pronouns = pronouns,
        bio = bio,
        banner = banner,
        accentColor = accentColor,
        publicFlags = publicFlags,
        privateFlags = privateFlags,
        verified = verified,
        email = email,
        phone = phone,
        mfaEnabled = mfaEnabled,
        locale = locale,
        purchasedFlags = purchasedFlags,
        premium = premium
    )
}

fun ApiAttachment.toEntity(): EntityAttachment {
    return EntityAttachment(
        id = id.value,
        filename = filename,
        size = size,
        url = url,
        proxyUrl = proxyUrl,
        width = width,
        height = height,
        contentType = contentType
    )
}

fun ApiEmbed.toEntity(): EntityEmbed {
    val entityAuthor = author?.toEntity()
    val entityFields = fields?.map { it.toEntity() }
    return EntityEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.rgbColor,
        timestamp = timestamp,
//        author = entityAuthor,
//        fields = entityFields
    )
}

fun ApiEmbedAuthor.toEntity(): EntityEmbedAuthor {
    return EntityEmbedAuthor(
        name = name
    )
}

fun ApiEmbedField.toEntity(): EntityEmbedField {
    return EntityEmbedField(
        name = name,
        value = value
    )
}

fun ApiMessageType.toEntity(): EntityMessageType {
    return EntityMessageType.fromValue(value) ?: EntityMessageType.Default
}

fun ApiChannel.toEntity(): EntityChannel {
    return EntityChannel(
        id = id.value,
        guildId = guildId?.value,
        name = name,
        type = type,
        position = position,
        parentId = parentId?.value,
        nsfw = nsfw,
        permissions = permissions.value,
    )
}