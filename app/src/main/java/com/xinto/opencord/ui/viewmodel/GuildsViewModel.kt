package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.store.Event
import com.xinto.opencord.store.GuildStore
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import kotlinx.coroutines.launch

class GuildsViewModel(
    guildStore: GuildStore,
    persistentDataManager: PersistentDataManager,
) : BasePersistenceViewModel(persistentDataManager) {

    sealed interface State {
        object Loading : State
        object Loaded : State
        object Error : State
    }

    var state by mutableStateOf<State>(State.Loading)
        private set

    val guilds = mutableStateMapOf<Long, DomainGuild>()
    var selectedGuildId by mutableStateOf(0L)
        private set

    fun selectGuild(guildId: Long) {
        selectedGuildId = guildId
        persistentGuildId = guildId
    }

    init {
        viewModelScope.launch {
            guildStore.observeGuilds().collect { event ->
                when (event) {
                    is Event.Add -> {
                        guilds[event.data.id] = event.data
                    }
                    is Event.Update -> {
                        guilds[event.data.id] = event.data
                    }
                    is Event.Remove -> {
                        guilds.remove(event.data?.id)
                    }
                }
            }
        }

        if (persistentGuildId != 0L) {
            selectedGuildId = persistentGuildId
        }
    }
}
