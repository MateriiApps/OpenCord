package com.xinto.opencord.ui.screens.home.panels.chat.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.xinto.opencord.domain.emoji.DomainEmoji

@Stable
class ReactionState(
    val emoji: DomainEmoji,
    val reactionOrder: Long,
    meReacted: Boolean,
    count: Int,
) {
    var meReacted by mutableStateOf(meReacted)
    var count by mutableStateOf(count)
}