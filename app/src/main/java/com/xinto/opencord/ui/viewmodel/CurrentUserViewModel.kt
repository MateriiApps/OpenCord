package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainUserStatus
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import kotlinx.coroutines.launch

class CurrentUserViewModel(
    gateway: DiscordGateway,
    repository: DiscordApiRepository
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
    var userCustomStatus by mutableStateOf<String?>(null)
        private set

    init {
        gateway.onEvent<ReadyEvent> {
            val domainUser = it.data.user.toDomain()
            avatarUrl = domainUser.avatarUrl
            username = domainUser.username
            discriminator = domainUser.formattedDiscriminator
        }

        viewModelScope.launch {
            try {
                val settings = repository.getUserSettings()
                userStatus = settings.status
                userCustomStatus = settings.customStatus?.text
                state = State.Loaded
            } catch (e: Throwable) {
                e.printStackTrace()
                state = State.Error
            }
        }
    }
}
