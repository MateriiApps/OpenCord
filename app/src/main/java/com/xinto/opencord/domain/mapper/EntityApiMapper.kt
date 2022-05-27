package com.xinto.opencord.domain.mapper

import com.xinto.opencord.db.entity.*
import com.xinto.opencord.rest.dto.*

fun EntityMessageFull.toApi(): ApiMessage {
    val apiId = ApiSnowflake(message.id)
    val apiChannelId = ApiSnowflake(message.channelId)
    val apiAuthor = message.author.toApi()
    val apiAttachments = attachments.map { it.toApi() }
//    val apiEmbeds = embeds.map { it.toApi() }
    val apiType = message.type.toApi()
    val apiContent = message.content
    val apiTimestamp = message.timestamp
    val apiEditedTimestamp = message.editedTimestamp
//    val apiReferencedMessage = referencedMessage?.toApi()
    return ApiMessage(
        id = apiId,
        channelId = apiChannelId,
        timestamp = apiTimestamp,
        editedTimestamp = apiEditedTimestamp,
        content = apiContent,
        author = apiAuthor,
        attachments = apiAttachments,
        embeds = emptyList() /*apiEmbeds*/,
        type = apiType,
//        referencedMessage = apiReferencedMessage
    )
}

fun EntityUser.toApi(): ApiUser {
    return ApiUser(
        id = ApiSnowflake(id),
        username = username,
        discriminator = discriminator,
        avatar = avatar,
        bot = bot
    )
}

fun EntityAttachment.toApi(): ApiAttachment {
    return ApiAttachment(
        id = ApiSnowflake(id),
        filename = filename,
        size = size,
        url = url,
        proxyUrl = proxyUrl,
        width = width,
        height = height,
        contentType = contentType
    )
}

fun EntityEmbed.toApi(): ApiEmbed {
//    val entityAuthor = author?.toApi()
//    val entityFields = fields?.map { it.toApi() }
    return ApiEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.let { ApiColor(it) },
        timestamp = timestamp,
//        author = entityAuthor,
//        fields = entityFields
    )
}

fun EntityEmbedAuthor.toApi(): ApiEmbedAuthor {
    return ApiEmbedAuthor(
        name = name
    )
}

fun EntityEmbedField.toApi(): ApiEmbedField {
    return ApiEmbedField(
        name = name,
        value = value
    )
}

fun EntityMessageType.toApi(): ApiMessageType {
    return ApiMessageType.fromValue(value) ?: ApiMessageType.Default
}