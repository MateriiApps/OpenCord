package com.xinto.opencord.domain.merger

import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.domain.model.DomainMessagePartial

fun DomainMessage.mergeWith(partial: DomainMessagePartial): DomainMessage {
    return DomainMessage(
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