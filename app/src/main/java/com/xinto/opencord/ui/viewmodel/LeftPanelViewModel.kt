package com.xinto.opencord.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.network.repository.DiscordAPIRepository
import com.xinto.opencord.network.result.DiscordAPIResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException

class LeftPanelViewModel(
    private val repository: DiscordAPIRepository
) : ViewModel() {

    private val _guilds = MutableStateFlow<DiscordAPIResult<List<DomainGuild>>>(DiscordAPIResult.Loading)
    val guilds: StateFlow<DiscordAPIResult<List<DomainGuild>>> = _guilds

    private val _currentGuild = MutableStateFlow<DomainGuild?>(null)
    val currentGuild: StateFlow<DomainGuild?> = _currentGuild

    private val _currentChannel = MutableStateFlow<DomainChannel?>(null)
    val currentChannel: StateFlow<DomainChannel?> = _currentChannel

    private val _currentChannelMessages = MutableStateFlow<List<DomainMessage>>(emptyList())
    val currentChannelMessage: StateFlow<List<DomainMessage>> = _currentChannelMessages

    suspend fun fetchGuilds() {
        _guilds.value =
            try {
                 DiscordAPIResult.Success(repository.getGuilds())
            } catch (e: HttpException) {
                DiscordAPIResult.Error(e)
            }
    }

    fun setCurrentGuild(domainGuild: DomainGuild) {
        _currentGuild.value = domainGuild
    }

}