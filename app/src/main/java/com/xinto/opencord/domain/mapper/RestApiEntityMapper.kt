package com.xinto.opencord.domain.mapper

import com.xinto.opencord.db.entity.message.EntityMessage
import com.xinto.opencord.rest.dto.ApiMessage

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
