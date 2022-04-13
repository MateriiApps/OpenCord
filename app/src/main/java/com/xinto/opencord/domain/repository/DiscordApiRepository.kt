package com.xinto.opencord.domain.repository

import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainChannel
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.domain.model.DomainMeGuild
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.util.ListMap

interface DiscordApiRepository {

    suspend fun getMeGuilds(): List<DomainMeGuild>

    suspend fun getGuild(guildId: ULong): DomainGuild

    suspend fun getGuildChannels(guildId: ULong): List<DomainChannel>

    suspend fun getChannel(channelId: ULong): DomainChannel

    suspend fun getChannelMessages(channelId: ULong): List<DomainMessage>

    suspend fun postChannelMessage(channelId: ULong, body: MessageBody)

}

class DiscordApiRepositoryImpl(
    gateway: DiscordGateway,
    private val service: DiscordApiService
) : DiscordApiRepository {

    private val cachedGuildChannels = ListMap<ULong, DomainChannel>()
    private val cachedChannelMessages = ListMap<ULong, DomainMessage>()

    private val cachedGuilds = mutableMapOf<ULong, DomainGuild>()
    private val cachedChannels = mutableMapOf<ULong, DomainChannel>()

    override suspend fun getMeGuilds(): List<DomainMeGuild> {
        return service.getMeGuilds().map { it.toDomain() }
    }

    override suspend fun getGuild(guildId: ULong): DomainGuild {
        if (cachedGuilds[guildId] == null) {
            cachedGuilds[guildId] = service.getGuild(guildId).toDomain()
        }

        return cachedGuilds[guildId]!!
    }

    override suspend fun getGuildChannels(guildId: ULong): List<DomainChannel> {
        if (cachedGuildChannels[guildId].isEmpty()) {
            val guildChannels = service.getGuildChannels(guildId).map { it.toDomain() }
            cachedGuildChannels[guildId].addAll(guildChannels)
        }
        return cachedGuildChannels[guildId]
    }

    override suspend fun getChannel(channelId: ULong): DomainChannel {
        if (cachedChannels[channelId] == null) {
            cachedChannels[channelId] = service.getChannel(channelId).toDomain()
        }
        return cachedChannels[channelId]!!
    }

    override suspend fun getChannelMessages(channelId: ULong): List<DomainMessage> {
        if (cachedChannelMessages[channelId].isEmpty()) {
            val channelMessages = service.getChannelMessages(channelId).map { it.toDomain() }
            cachedChannelMessages[channelId].addAll(channelMessages)
        }
        return cachedChannelMessages[channelId]
    }

    override suspend fun postChannelMessage(channelId: ULong, body: MessageBody) {
        service.postChannelMessage(channelId, body)
    }

    init {
        gateway.onEvent<MessageCreateEvent> {
            val domainData = it.data.toDomain()
            cachedChannelMessages[domainData.channelId].add(domainData)
        }
    }
}