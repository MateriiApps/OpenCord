package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.guild.DomainGuild
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.store.GuildStore
import com.xinto.opencord.store.fold
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel
import com.xinto.opencord.util.collectIn
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
            guilds.putAll(guildStore.fetchGuilds().associateBy { it.id })
            state = State.Loaded
        }

        guildStore.observeGuilds().collectIn(viewModelScope) { event ->
            state = State.Loaded
            event.fold(
                onAdd = { guilds[it.id] = it },
                onUpdate = { guilds[it.id] = it },
                onDelete = { guilds.remove(it) },
            )
        }

        if (persistentGuildId != 0L) {
            selectedGuildId = persistentGuildId
        }
    }
}
