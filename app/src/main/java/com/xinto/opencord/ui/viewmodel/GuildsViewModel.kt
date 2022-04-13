package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.model.DomainMeGuild
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import kotlinx.coroutines.launch

class GuildsViewModel(
    gateway: DiscordGateway,
    persistentDataManager: PersistentDataManager,
    private val repository: DiscordApiRepository
) : BasePersistenceViewModel(persistentDataManager) {

    sealed interface State {
        object Loading : State
        object Loaded : State
        object Error : State
    }

    var state by mutableStateOf<State>(State.Loading)
        private set

    val guilds = mutableStateListOf<DomainMeGuild>()
    var selectedGuildId by mutableStateOf(0UL)
        private set

    fun load() {
        viewModelScope.launch {
            try {
                state = State.Loading
                val meGuilds = repository.getMeGuilds()
                guilds.clear()
                guilds.addAll(meGuilds)
                state = State.Loaded
            } catch (e: Exception) {
                state = State.Error
                e.printStackTrace()
            }
        }
    }

    fun selectGuild(guildId: ULong) {
        selectedGuildId = guildId
        persistentGuildId = guildId
    }

    init {
        load()

        if (persistentGuildId != 0UL) {
            selectedGuildId = persistentGuildId
        }
    }

}