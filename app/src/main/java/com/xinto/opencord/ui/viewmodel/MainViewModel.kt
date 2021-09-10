package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.network.body.MessageBody
import com.xinto.opencord.network.gateway.Gateway
import com.xinto.opencord.network.gateway.event.message.MessageCreateEvent
import com.xinto.opencord.network.repository.DiscordAPIRepository
import com.xinto.opencord.network.result.DiscordAPIResult
import com.xinto.opencord.util.getSortedChannels
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(
    private val gateway: Gateway,
    private val repository: DiscordAPIRepository,
) : ViewModel() {

    data class ChannelListData(
        val bannerUrl: String?,
        val channels: Map<DomainChannel.Category?, List<DomainChannel>>
    )

    private val _guilds =
        MutableStateFlow<DiscordAPIResult<List<DomainGuild>>>(DiscordAPIResult.Loading)
    val guilds: StateFlow<DiscordAPIResult<List<DomainGuild>>> = _guilds

    private val _currentGuild = MutableStateFlow<DomainGuild?>(null)
    val currentGuild: StateFlow<DomainGuild?> = _currentGuild

    private val _currentGuildChannels =
        MutableStateFlow<DiscordAPIResult<ChannelListData>>(DiscordAPIResult.Loading)
    val currentGuildChannels: StateFlow<DiscordAPIResult<ChannelListData>> = _currentGuildChannels

    private val _currentChannel = MutableStateFlow<DomainChannel?>(null)
    val currentChannel: StateFlow<DomainChannel?> = _currentChannel

    private val _currentChannelMessages =
        MutableStateFlow<DiscordAPIResult<SnapshotStateList<DomainMessage>>>(DiscordAPIResult.Loading)
    val currentChannelMessages: StateFlow<DiscordAPIResult<SnapshotStateList<DomainMessage>>> =
        _currentChannelMessages

    fun setCurrentGuild(guild: DomainGuild) {
        _currentGuild.value = guild
        fetchChannelsForGuild(guild)
    }

    fun setCurrentChannel(channel: DomainChannel) {
        _currentChannel.value = channel
        fetchMessagesForChannel(channel.channelId)
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            repository.postChannelMessage(
                channelId = _currentChannel.value!!.channelId,
                messageBody = MessageBody(message)
            )
        }
    }

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

    fun fetchChannelsForGuild(guild: DomainGuild) {
        viewModelScope.launch {
            _currentGuildChannels.value =
                try {
                    DiscordAPIResult.Success(
                        ChannelListData(
                            bannerUrl = guild.bannerUrl,
                            channels = getSortedChannels(
                                repository
                                    .getGuildChannels(guild.id)
                                    .toMutableStateList()
                            )
                        )
                    )
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

    init {
        fetchGuilds()
        gateway.onEvent { event ->
            when (event) {
                is MessageCreateEvent -> {
                    val message = DomainMessage.fromApi(event.message)

                    if (message.channelId == currentChannel.value?.channelId) {
                        val currentChannelMessages = _currentChannelMessages.value
                        if (currentChannelMessages is DiscordAPIResult.Success) {
                            val data = currentChannelMessages.data
                            if (!data.contains(message)) {
                                data.add(0, message)
                            }
                        }
                    }
                }
            }
        }
    }

}