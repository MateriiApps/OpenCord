package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.emoji.DomainEmoji
import com.xinto.opencord.domain.emoji.DomainUnicodeEmoji
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.store.CurrentUserStore
import com.xinto.opencord.store.MessageStore
import com.xinto.opencord.store.fold
import com.xinto.opencord.util.collectIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MessageMenuViewModel(
    messageId: Long,
    private val messages: MessageStore,
    private val currentUserStore: CurrentUserStore,
) : ViewModel() {
    sealed interface State {
        object Loading : State
        object Loaded : State
        object Closing : State
    }

    var state by mutableStateOf<State>(State.Loading)
        private set

    var message by mutableStateOf<DomainMessage?>(null)
        private set
    var isEditable by mutableStateOf(false)
        private set
    var isDeletable by mutableStateOf(false)
        private set
    var isPinnable by mutableStateOf(false)
        private set

    val frequentReactions = mutableListOf<DomainEmoji>()

    fun onReply() {}
    fun onEdit() {}
    fun onDelete() {}

    fun onCopyLink() {

    }

    fun onCopyMessage() {

    }

    fun onMarkUnread() {

    }

    fun onPin() {

    }

    fun onCopyId() {

    }

    init {
        viewModelScope.launch {
            delay(1000)

            val message = messages.getMessage(messageId)
            val currentUser = currentUserStore.getCurrentUser()

            if (message == null || currentUser == null) {
                state = State.Closing
                return@launch
            }

            // TODO: frequent emojis
            val emojis = arrayOf(
                "\uD83D\uDDFF",
                "\uD83D\uDC80",
                "\uD83D\uDE2D",
                "\uD83D\uDE15",
                "\uD83D\uDE44",
                "⭐",
            )
            frequentReactions.addAll(
                emojis.map {
                    DomainUnicodeEmoji(it)
                },
            )

            // TODO: channel perms
            isEditable = currentUser.id == message.author.id
            isDeletable = currentUser.id == message.author.id
            isPinnable = false
            this@MessageMenuViewModel.message = message
            state = State.Loaded
        }

        messages.observeMessage(messageId).collectIn(viewModelScope) { event ->
            event.fold(
                onAdd = {},
                onUpdate = { message = it },
                onDelete = { state = State.Closing },
            )
        }
    }
}
