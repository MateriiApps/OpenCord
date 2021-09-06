package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.network.gateway.Gateway
import com.xinto.opencord.network.gateway.event.message.MessageCreateEvent
import com.xinto.opencord.network.repository.DiscordAPIRepository
import com.xinto.opencord.network.result.DiscordAPIResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(
    private val gateway: Gateway,
    private val repository: DiscordAPIRepository,
) : ViewModel() {

    private val _guilds = MutableStateFlow<DiscordAPIResult<List<DomainGuild>>>(DiscordAPIResult.Loading)
    val guilds: StateFlow<DiscordAPIResult<List<DomainGuild>>> = _guilds

    private val _currentGuild = MutableStateFlow<DomainGuild?>(null)
    val currentGuild: StateFlow<DomainGuild?> = _currentGuild

    private val _currentChannel = MutableStateFlow<DomainChannel?>(null)
    val currentChannel: StateFlow<DomainChannel?> = _currentChannel

    private val _currentChannelMessages = MutableStateFlow<DiscordAPIResult<SnapshotStateList<DomainMessage>>>(DiscordAPIResult.Loading)
    val currentChannelMessages: StateFlow<DiscordAPIResult<SnapshotStateList<DomainMessage>>> = _currentChannelMessages

    fun fetchGuilds() {
        viewModelScope.launch {
            _guilds.value =
                try {
                    DiscordAPIResult.Success(repository.getGuilds())
                } catch (e: HttpException) {
                    DiscordAPIResult.Error(e)
                }
        }
    }

    fun fetchMessagesForChannel(channelId: Long) {
        viewModelScope.launch {
            _currentChannelMessages.value =
                try {
                    DiscordAPIResult.Success(
                        repository
                            .getChannelMessages(channelId)
                            .toMutableStateList()
                    )
                } catch (e: HttpException) {
                    DiscordAPIResult.Error(e)
                }
        }
    }

    fun setCurrentGuild(guild: DomainGuild) {
        _currentGuild.value = guild
    }

    fun setCurrentChannel(channel: DomainChannel) {
        _currentChannel.value = channel
        fetchMessagesForChannel(channel.channelId)
    }

    init {
        fetchGuilds()
        gateway.onEvent { event ->
            when (event) {
                is MessageCreateEvent -> {
                    val message = DomainMessage.fromApi(event.message)

                    if (message.channelId == currentChannel.value?.channelId) {
                        //Queue until data loads
                        viewModelScope.launch {
                            _currentChannelMessages.collect {
                                if (it is DiscordAPIResult.Success) {
                                    if (!it.data.contains(message)) {
                                        it.data.add(0, message)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}