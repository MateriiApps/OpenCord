package com.xinto.opencord.domain.channel

import androidx.compose.runtime.Immutable
import com.xinto.opencord.db.entity.channel.EntityUnreadState

@Immutable
data class DomainUnreadState(
    val channelId: Long,
    val mentionCount: Int,
    val lastMessageId: Long,
)

fun EntityUnreadState.toDomain(): DomainUnreadState {
    return DomainUnreadState(
        channelId = channelId,
        mentionCount = mentionCount,
        lastMessageId = lastMessageId,
    )
}
