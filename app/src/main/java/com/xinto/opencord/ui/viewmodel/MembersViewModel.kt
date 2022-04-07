package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.xinto.opencord.domain.model.DomainGuildMember
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.GuildMemberChunkEvent
import com.xinto.opencord.gateway.onEvent

class MembersViewModel(
    gateway: DiscordGateway,
    private val repository: DiscordApiRepository
) : ViewModel() {

    val members = mutableStateListOf<DomainGuildMember>()

    fun load() {

    }

    init {
        gateway.onEvent<GuildMemberChunkEvent> {

        }
    }
}