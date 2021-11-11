package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.domain.model.DomainMeGuild
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.network.body.MessageBody
import com.xinto.opencord.network.gateway.Gateway
import com.xinto.opencord.network.gateway.event.message.MessageCreateEvent
import com.xinto.opencord.network.repository.DiscordAPIRepository
import com.xinto.opencord.network.result.DiscordAPIResult
import com.xinto.opencord.util.getSortedChannels
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

    sealed class CurrentGuild {
        object None : CurrentGuild()
        data class Guild(val data: DomainGuild) : CurrentGuild()
    }

    sealed class CurrentChannel {
        object None : CurrentChannel()
        data class Channel(val data: DomainChannel) : CurrentChannel()
    }

    var meGuilds by mutableStateOf<DiscordAPIResult<List<DomainMeGuild>>>(DiscordAPIResult.Loading)
        private set

    var currentGuild by mutableStateOf<CurrentGuild>(CurrentGuild.None)
        private set

    var currentChannel by mutableStateOf<CurrentChannel>(CurrentChannel.None)
        private set

    private val _guilds = mutableStateMapOf<Long, DomainGuild>()
    val guilds: SnapshotStateMap<Long, DomainGuild> = _guilds

    private val _channels = mutableStateMapOf<Long, ChannelListData>()
    val channels: SnapshotStateMap<Long, ChannelListData> = _channels

    private val _messages = mutableStateMapOf<Long, MessageListData>()
    val messages: SnapshotStateMap<Long, MessageListData> = _messages

    suspend fun setCurrentGuild(meGuild: DomainMeGuild) {
        if (guilds[meGuild.id] == null)  {
            guilds[meGuild.id] = repository.getGuild(meGuild.id)
        }

        val guild = guilds[meGuild.id]!!

        currentGuild = CurrentGuild.Guild(guild)

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
        currentChannel = CurrentChannel.Channel(channel)

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
        val currentChannelAsChannel = currentChannel as? CurrentChannel.Channel

        if (currentChannelAsChannel != null) {
            viewModelScope.launch {
                repository.postChannelMessage(
                    channelId =currentChannelAsChannel.data.channelId,
                    messageBody = MessageBody(message)
                )
            }
        }
    }

    fun fetchGuilds() {
        viewModelScope.launch {
            meGuilds =
                try {
                    DiscordAPIResult.Success(repository.getMeGuilds())
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