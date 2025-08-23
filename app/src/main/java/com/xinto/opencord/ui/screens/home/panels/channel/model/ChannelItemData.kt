package com.xinto.opencord.ui.screens.home.panels.channel.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.xinto.opencord.domain.channel.DomainChannel
import com.xinto.opencord.domain.channel.DomainUnreadState
import kotlinx.coroutines.Job

@Stable
class ChannelItemData(
    channel: DomainChannel,
    mentionCount: Int,
    var unreadListenerJob: Job? = null,
    var lastMessageListenerJob: Job? = null,
    var mentionCountListenerJob: Job? = null,
    private var lastUnreadMessageId: Long? = null,
    private var lastChannelMessageId: Long? = null,
) {
    private val _isUnread: Boolean
        get() = (lastChannelMessageId ?: 0) > (lastUnreadMessageId ?: 0)

    var channel by mutableStateOf(channel)
    var mentionCount by mutableStateOf(mentionCount)
    var isUnread by mutableStateOf(_isUnread)
        private set

    fun updateUnreadState(unreadState: DomainUnreadState?) {
        lastUnreadMessageId = unreadState?.lastMessageId
        mentionCount = unreadState?.mentionCount ?: 0
        isUnread = _isUnread
    }

    fun updateLastMessageId(lastMessageId: Long?) {
        this.lastChannelMessageId = lastMessageId
        isUnread = _isUnread
    }

    fun cancelJobs() {
        unreadListenerJob?.cancel()
        lastMessageListenerJob?.cancel()
        mentionCountListenerJob?.cancel()
    }
}