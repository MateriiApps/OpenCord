package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.dto.ApiStatus
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

    var status by mutableStateOf<ApiStatus?>(null)
        private set
    var customStatus by mutableStateOf<String?>(null)
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
                status = settings.status
                customStatus = settings.customStatus
                state = State.Loaded
            } catch (e: Throwable) {
                e.printStackTrace()
                state = State.Error
            }
        }
    }
}
