package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.GuildCreateEvent
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel

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

    var state by mutableStateOf<State>(State.Loaded)
        private set

    val guilds = mutableStateMapOf<ULong, DomainGuild>()
    var selectedGuildId by mutableStateOf(0UL)
        private set

    fun load() {
//        viewModelScope.launch {
//            try {
//                state = State.Loading
//                val meGuilds = repository.getMeGuilds()
//                guilds.clear()
//                guilds.addAll(meGuilds)
//                state = State.Loaded
//            } catch (e: Exception) {
//                state = State.Error
//                e.printStackTrace()
//            }
//        }
    }

    fun selectGuild(guildId: ULong) {
        selectedGuildId = guildId
        persistentGuildId = guildId
    }

    init {
//        load()

        gateway.onEvent<ReadyEvent> { event ->
            event.data.guilds.forEach {
                val domainGuild = it.toDomain()
                guilds[domainGuild.id] = domainGuild
            }
        }

        gateway.onEvent<GuildCreateEvent> {
            val domainGuild = it.data.toDomain()
            guilds[domainGuild.id] = domainGuild
        }

        if (persistentGuildId != 0UL) {
            selectedGuildId = persistentGuildId
        }
    }

}