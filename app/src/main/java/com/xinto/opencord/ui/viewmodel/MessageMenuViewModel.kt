package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.emoji.DomainEmoji
import com.xinto.opencord.domain.emoji.DomainUnicodeEmoji
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.manager.ClipboardManager
import com.xinto.opencord.manager.ToastManager
import com.xinto.opencord.store.CurrentUserStore
import com.xinto.opencord.store.MessageStore
import com.xinto.opencord.store.fold
import com.xinto.opencord.util.collectIn
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MessageMenuViewModel(
    messageId: Long,
    private val messages: MessageStore,
    private val currentUserStore: CurrentUserStore,
    private val clipboard: ClipboardManager,
    private val toasts: ToastManager,
) : ViewModel() {
    sealed interface State {
        object Loading : State
        object Loaded : State
        object Closing : State
    }

    sealed interface PinState {
        object Pinnable : PinState
        object Unpinnable : PinState
        object None : PinState
    }

    var state by mutableStateOf<State>(State.Loading)
        private set

    var message by mutableStateOf<DomainMessage?>(null)
        private set
    var isEditable by mutableStateOf(false)
        private set
    var isDeletable by mutableStateOf(false)
        private set
    var pinState by mutableStateOf<PinState>(PinState.None)
        private set

    val frequentReactions = mutableListOf<DomainEmoji>()

    fun onReply() {}
    fun onEdit() {}
    fun onDelete() {}

    fun onCopyLink() {
        // TODO: add guildId to DomainMessage
    }

    fun onCopyMessage() {
        clipboard.setText(message?.content ?: return)
        toasts.showToast("Copied message!")
    }

    fun onMarkUnread() {}
    fun togglePinned() {}

    fun onCopyId() {
        val id = message?.id ?: return
        clipboard.setText(id.toString())

        toasts.showToast("Copied message ID!")
    }

    init {
        viewModelScope.launch {
            // replay or wait for gw to connect
            val currentUser = currentUserStore.observeCurrentUser().firstOrNull()
            val message = messages.getMessage(messageId)

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

            // TODO: message permissions
            pinState = PinState.None
            isDeletable = currentUser.id == message.author.id
            isEditable = currentUser.id == message.author.id
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

        currentUserStore.observeCurrentUser().collectIn(viewModelScope) { user ->
            pinState = PinState.None
            isDeletable = user.id == message?.author?.id
            isEditable = user.id == message?.author?.id
        }
    }
}
