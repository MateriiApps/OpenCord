package com.xinto.opencord.domain.repository

import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.domain.model.DomainMeGuild
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.service.DiscordApiService

interface DiscordApiRepository {

    suspend fun getMeGuilds(): List<DomainMeGuild>

    suspend fun getGuild(guildId: ULong): DomainGuild

    suspend fun getGuildChannels(guildId: ULong): List<DomainChannel>

    suspend fun getChannel(channelId: ULong): DomainChannel

    suspend fun getChannelMessages(channelId: ULong): List<DomainMessage>

    suspend fun postChannelMessage(channelId: ULong, body: MessageBody)

}

class DiscordApiRepositoryImpl(
    private val service: DiscordApiService
) : DiscordApiRepository {

    override suspend fun getMeGuilds(): List<DomainMeGuild> {
        return service.getMeGuilds().map { it.toDomain() }
    }

    override suspend fun getGuild(guildId: ULong): DomainGuild {
        return service.getGuild(guildId).toDomain()
    }

    override suspend fun getGuildChannels(guildId: ULong): List<DomainChannel> {
        return service.getGuildChannels(guildId).map { it.toDomain() }
    }

    override suspend fun getChannel(channelId: ULong): DomainChannel {
        return service.getChannel(channelId).toDomain()
    }

    override suspend fun getChannelMessages(channelId: ULong): List<DomainMessage> {
        return service.getChannelMessages(channelId)
            .map { it.toDomain() }
            .sortedBy { it.timestamp }
    }

    override suspend fun postChannelMessage(channelId: ULong, body: MessageBody) {
        service.postChannelMessage(channelId, body)
    }
}