package com.xinto.opencord.domain.merger

import com.xinto.opencord.rest.dto.ApiMessage
import com.xinto.opencord.rest.dto.ApiMessagePartial

fun ApiMessage.mergeWith(partial: ApiMessagePartial): ApiMessage {
    return ApiMessage(
        id = partial.id,
        channelId = partial.channelId,
        timestamp = partial.timestamp ?: timestamp,
        editedTimestamp = partial.editedTimestamp ?: editedTimestamp,
        content = partial.content ?: content,
        author = partial.author ?: author,
        attachments = partial.attachments ?: attachments,
        embeds = partial.embeds ?: embeds
    )
}