package com.xinto.opencord.domain.repository

import com.xinto.opencord.domain.mapper.toApi
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.*
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.service.DiscordApiService

interface DiscordApiRepository {
    suspend fun getMeGuilds(): List<DomainMeGuild>
    suspend fun getGuild(guildId: Long): DomainGuild
    suspend fun getGuildChannels(guildId: Long): Map<Long, DomainChannel>

    suspend fun getChannel(channelId: Long): DomainChannel
    suspend fun getChannelMessages(channelId: Long): Map<Long, DomainMessage>
    suspend fun getChannelPins(channelId: Long): Map<Long, DomainMessage>

    suspend fun postChannelMessage(channelId: Long, body: MessageBody)

    suspend fun getUserSettings(): DomainUserSettings
    suspend fun updateUserSettings(settings: DomainUserSettingsPartial): DomainUserSettings

    suspend fun startTyping(channelId: Long)
}

class DiscordApiRepositoryImpl(
    private val service: DiscordApiService
) : DiscordApiRepository {

    override suspend fun getMeGuilds(): List<DomainMeGuild> {
        return service.getMeGuilds().map { it.toDomain() }
    }

    override suspend fun getGuild(guildId: Long): DomainGuild {
        return service.getGuild(guildId).toDomain()
    }

    override suspend fun getGuildChannels(guildId: Long): Map<Long, DomainChannel> {
        return service.getGuildChannels(guildId)
            .toList().associate {
                it.first.value to it.second.toDomain()
            }
    }

    override suspend fun getChannel(channelId: Long): DomainChannel {
        return service.getChannel(channelId).toDomain()
    }

    override suspend fun getChannelMessages(channelId: Long): Map<Long, DomainMessage> {
        return service.getChannelMessages(channelId)
            .toList().associate {
                it.first.value to it.second.toDomain()
            }
    }

    override suspend fun getChannelPins(channelId: Long): Map<Long, DomainMessage> {
        return service.getChannelPins(channelId)
            .toList().associate {
                it.first.value to it.second.toDomain()
            }
    }

    override suspend fun postChannelMessage(channelId: Long, body: MessageBody) {
        service.postChannelMessage(channelId, body)
    }

    override suspend fun getUserSettings(): DomainUserSettings {
        return service.getUserSettings().toDomain()
    }

    override suspend fun updateUserSettings(settings: DomainUserSettingsPartial): DomainUserSettings {
        return service.updateUserSettings(settings.toApi()).toDomain()
    }

    override suspend fun startTyping(channelId: Long) {
        service.startTyping(channelId)
    }
}
