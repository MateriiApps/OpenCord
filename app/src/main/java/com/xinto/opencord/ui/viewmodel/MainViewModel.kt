package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
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
        val channels: Map<DomainChannel.Category?, List<DomainChannel>>,
    )

    data class MessageListData(
        val messages: SnapshotStateList<DomainMessage>
    )

    private val _guilds =
        MutableStateFlow<DiscordAPIResult<List<DomainGuild>>>(DiscordAPIResult.Loading)
    val guilds: StateFlow<DiscordAPIResult<List<DomainGuild>>> = _guilds

    private val _currentGuild = MutableStateFlow<DomainGuild?>(null)
    val currentGuild: StateFlow<DomainGuild?> = _currentGuild

    private val _currentChannel = MutableStateFlow<DomainChannel?>(null)
    val currentChannel: StateFlow<DomainChannel?> = _currentChannel

    private val _channels = mutableStateMapOf<Long, ChannelListData>()
    val channels: SnapshotStateMap<Long, ChannelListData> = _channels

    private val _messages = mutableStateMapOf<Long, MessageListData>()
    val messages: SnapshotStateMap<Long, MessageListData> = _messages

    suspend fun setCurrentGuild(guild: DomainGuild) {
        _currentGuild.value = guild

        if (_channels[guild.id] == null) {
            try {
                _channels[guild.id] = ChannelListData(
                    bannerUrl = guild.bannerUrl,
                    channels = getSortedChannels(
                        repository.getGuildChannels(guild.id)
                    )
                )
            } catch (e: HttpException) {
            }
        }
    }

    suspend fun setCurrentChannel(channel: DomainChannel) {
        _currentChannel.value = channel

        if (_messages[channel.channelId] == null) {
            try {
                _messages[channel.channelId] = MessageListData(
                    messages = repository
                        .getChannelMessages(channel.channelId)
                        .toMutableStateList()
                )
            } catch (e: HttpException) {
            }
        }
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

    init {
        fetchGuilds()
        gateway.onEvent<MessageCreateEvent> {
            val domainMessage = DomainMessage.fromApi(message)

            _messages[domainMessage.channelId]?.messages?.add(0, domainMessage)
        }
    }

}