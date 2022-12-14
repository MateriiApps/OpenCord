package com.xinto.opencord.ui.viewmodel

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.materiiapps.partial.Partial
import com.xinto.opencord.R
import com.xinto.opencord.domain.activity.DomainActivityEmoji
import com.xinto.opencord.domain.activity.types.DomainActivityCustom
import com.xinto.opencord.domain.activity.types.DomainActivityStreaming
import com.xinto.opencord.domain.usersettings.DomainCustomStatus
import com.xinto.opencord.domain.usersettings.DomainUserSettingsPartial
import com.xinto.opencord.domain.usersettings.DomainUserStatus
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.dto.UpdatePresence
import com.xinto.opencord.rest.models.activity.toApi
import com.xinto.opencord.store.CurrentUserStore
import com.xinto.opencord.store.SessionStore
import com.xinto.opencord.store.UserSettingsStore
import com.xinto.opencord.util.collectIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class CurrentUserViewModel(
    private val gateway: DiscordGateway,
    private val sessionStore: SessionStore,
    private val currentUserStore: CurrentUserStore,
    private val userSettingsStore: UserSettingsStore,
) : ViewModel() {
    sealed interface State {
        object Loading : State
        object Loaded : State
        object Error : State
    }

    var state by mutableStateOf<State>(State.Loading)
        private set

    var avatarUrl by mutableStateOf("")
        private set
    var username by mutableStateOf("")
        private set
    var discriminator by mutableStateOf("")
        private set

    var userStatus by mutableStateOf<DomainUserStatus?>(null)
        private set
    var userCustomStatus by mutableStateOf<DomainCustomStatus?>(null)
        private set
    var isStreaming by mutableStateOf(false)
        private set

    fun setStatus(@DrawableRes icon: Int) {
        viewModelScope.launch {
            val status = when (icon) {
                R.drawable.ic_status_online -> DomainUserStatus.Online
                R.drawable.ic_status_idle -> DomainUserStatus.Idle
                R.drawable.ic_status_dnd -> DomainUserStatus.Dnd
                R.drawable.ic_status_invisible -> DomainUserStatus.Invisible
                else -> throw IllegalStateException("Unknown status icon!")
            }

            gateway.updatePresence(
                UpdatePresence(
                    status = status.value,
                    afk = null,
                    since = Clock.System.now().toEpochMilliseconds(),
                    activities = sessionStore.getActivities()?.map { it.toApi() } ?: return@launch,
                ),
            )

            userSettingsStore.updateUserSettings(
                DomainUserSettingsPartial(
                    status = Partial.Value(status),
                ),
            )
        }
    }

    fun setCustomStatus(status: DomainCustomStatus?) {
        viewModelScope.launch {
            val currentStatus = sessionStore.getCurrentSession()?.status
                ?: return@launch
            val currentActivities = sessionStore.getActivities()
                ?.filter { it !is DomainActivityCustom }
                ?.toMutableList()
                ?: return@launch

            userSettingsStore.updateUserSettings(
                DomainUserSettingsPartial(
                    customStatus = Partial.Value(status),
                ),
            )

            val currentMillis = Clock.System.now().toEpochMilliseconds()

            if (status != null) {
                currentActivities += DomainActivityCustom(
                    name = "Custom Status",
                    status = status.text,
                    createdAt = currentMillis,
                    emoji = if (status.emojiId == null || status.emojiName == null) null else {
                        DomainActivityEmoji(
                            name = status.emojiName,
                            id = status.emojiId,
                            animated = false, // TODO: fix this
                        )
                    },
                )
            }

            gateway.updatePresence(
                UpdatePresence(
                    status = currentStatus,
                    since = currentMillis,
                    afk = null,
                    activities = currentActivities.map { it.toApi() },
                ),
            )
        }
    }

    init {
        currentUserStore.observeCurrentUser().collectIn(viewModelScope) { user ->
            avatarUrl = user.avatarUrl
            username = user.username
            discriminator = user.formattedDiscriminator
            state = State.Loaded
        }

        userSettingsStore.observeUserSettings().collectIn(viewModelScope) { event ->
            userStatus = event.status
            userCustomStatus = event.customStatus
        }

        sessionStore.observeActivities().collectIn(viewModelScope) { event ->
            isStreaming = event.any { it is DomainActivityStreaming }
        }
    }
}
