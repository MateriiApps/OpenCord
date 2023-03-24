package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import com.xinto.opencord.util.throttle
import kotlinx.coroutines.launch

@Stable
class ChatInputViewModel(
    private val api: DiscordApiService,
    persistentDataManager: PersistentDataManager,
) : BasePersistenceViewModel(persistentDataManager) {
    var sendEnabled by mutableStateOf(true)
        private set
    var pendingContent by mutableStateOf("", neverEqualPolicy())
        private set

    fun setPendingMessage(content: String) {
        pendingContent = content
        startTyping()
    }

    fun sendMessage() {
        val content = pendingContent
        pendingContent = ""

        if (content.isBlank()) {
            return
        }

        sendEnabled = false

        val message = MessageBody(
            content = content,
        )

        viewModelScope.launch {
            api.postChannelMessage(
                channelId = persistentChannelId,
                body = message,
            )
            sendEnabled = true
        }
    }

    private val startTyping = throttle(9500, viewModelScope) {
        api.startTyping(persistentDataManager.persistentChannelId)
    }
}
