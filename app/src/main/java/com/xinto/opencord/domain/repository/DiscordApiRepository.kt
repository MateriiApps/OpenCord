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

    suspend fun getGuild(guildId: Long): DomainGuild

    suspend fun getGuildChannels(guildId: Long): List<DomainChannel>

    suspend fun getChannelMessages(channelId: Long): List<DomainMessage>

    suspend fun postChannelMessage(channelId: Long, body: MessageBody)

}

class DiscordApiRepositoryImpl(
    gateway: DiscordGateway,
    private val service: DiscordApiService
) : DiscordApiRepository {

    private val guildChannels = mutableListOf<DomainChannel>()
    private val cachedGuildChannels = ListMap<Long, DomainChannel>()
    private val cachedChannelMessages = ListMap<Long, DomainMessage>()

    override suspend fun getMeGuilds(): List<DomainMeGuild> {
        return service.getMeGuilds().map { it.toDomain() }
    }

    override suspend fun getGuild(guildId: Long): DomainGuild {
        return service.getGuild(guildId).toDomain()
    }

    override suspend fun getGuildChannels(guildId: Long): List<DomainChannel> {
        if (cachedGuildChannels[guildId].isEmpty()) {
            val guildChannels = service.getGuildChannels(guildId).map { it.toDomain() }
            cachedGuildChannels[guildId].addAll(guildChannels)
        }
        return cachedGuildChannels[guildId]
    }

    override suspend fun getChannelMessages(channelId: Long): List<DomainMessage> {
        if (cachedChannelMessages[channelId].isEmpty()) {
            val channelMessages = service.getChannelMessages(channelId).map { it.toDomain() }
            cachedChannelMessages[channelId].addAll(channelMessages)
        }
        return cachedChannelMessages[channelId]
    }

    override suspend fun postChannelMessage(channelId: Long, body: MessageBody) {
        service.postChannelMessage(channelId, body)
    }

    init {
        gateway.onEvent<MessageCreateEvent> {
            val domainData = it.data.toDomain()
            cachedChannelMessages[domainData.channelId].add(domainData)
        }
    }
}