package com.xinto.opencord.ui.viewmodel

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.R
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.*
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.event.UserSettingsUpdateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.partialgen.PartialValue
import kotlinx.coroutines.launch

class CurrentUserViewModel(
    gateway: DiscordGateway,
    private val repository: DiscordApiRepository
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

    private var userSettings: DomainUserSettings? = null

    fun setStatus(@DrawableRes icon: Int) {
        viewModelScope.launch {
            val status = when (icon) {
                R.drawable.ic_status_online -> DomainUserStatus.Online
                R.drawable.ic_status_idle -> DomainUserStatus.Idle
                R.drawable.ic_status_dnd -> DomainUserStatus.Dnd
                R.drawable.ic_status_invisible -> DomainUserStatus.Invisible
                else -> throw IllegalStateException("Unknown status icon!")
            }

            val settings = DomainUserSettingsPartial(status = PartialValue.Value(status))
            repository.updateUserSettings(settings)
        }
        // TODO: send a presence update
    }

    fun setCustomStatus(status: DomainCustomStatus?) {
        viewModelScope.launch {
            val settings = DomainUserSettingsPartial(
                customStatus = PartialValue.toPartial(status)
            )
            repository.updateUserSettings(settings)
        }
        // TODO: send a presence update
    }

    init {
        gateway.onEvent<ReadyEvent> {
            val domainUser = it.data.user.toDomain()
            avatarUrl = domainUser.avatarUrl
            username = domainUser.username
            discriminator = domainUser.formattedDiscriminator
        }
        gateway.onEvent<UserSettingsUpdateEvent> {
            val mergedData = userSettings?.merge(it.data.toDomain())
                .also { mergedData -> userSettings = mergedData }
            userStatus = mergedData?.status
            userCustomStatus = mergedData?.customStatus
            println(mergedData?.customStatus?.text)
        }

        viewModelScope.launch {
            try {
                val settings = repository.getUserSettings()
                    .also {
                        userSettings = it
                    }
                userStatus = settings.status
                userCustomStatus = settings.customStatus
                state = State.Loaded
            } catch (e: Throwable) {
                e.printStackTrace()
                state = State.Error
            }
        }
    }
}
