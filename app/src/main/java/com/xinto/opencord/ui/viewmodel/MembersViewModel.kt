package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainGuildMember
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.GuildMemberChunkEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.gateway.scheduleOnConnection
import com.xinto.opencord.ui.viewmodel.base.BasePersistenceViewModel

class MembersViewModel(
    persistentDataManager: PersistentDataManager,
    private val gateway: DiscordGateway,
    private val repository: DiscordApiRepository,
) : BasePersistenceViewModel(persistentDataManager) {

    val members = mutableStateListOf<DomainGuildMember>()

    fun load() {
//        viewModelScope.launch {
//            gateway.requestGuildMembers(persistentGuildId)
//        }
    }

    init {
        gateway.onEvent<GuildMemberChunkEvent> {
            val domainMembers = it.data.toDomain().guildMembers
            members.addAll(domainMembers)
        }

        if (persistentGuildId != 0UL) {
            gateway.scheduleOnConnection {
                load()
            }
        }
    }
}