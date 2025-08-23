package com.xinto.opencord.ui.screens.home.panels.chat.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.xinto.opencord.domain.emoji.DomainEmojiIdentifier
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.DomainMessageRegular

@Stable
class MessageItem(
    message: DomainMessage,
    reactions: List<ReactionState>? = null,
    topMerged: Boolean = false,
    currentUserId: Long?,
) {
    var topMerged by mutableStateOf(topMerged)
    var bottomMerged by mutableStateOf(false)
    var message by mutableStateOf(message)
    var reactions = mutableStateMapOf<DomainEmojiIdentifier, ReactionState>()
        .apply { reactions?.let { putAll(it.map { r -> r.emoji.identifier to r }) } }

    val meMentioned by derivedStateOf {
        when {
            message !is DomainMessageRegular -> false
            message.mentionEveryone -> true
            message.mentions.any { it.id == currentUserId } -> true
            else -> false
        }
    }
}