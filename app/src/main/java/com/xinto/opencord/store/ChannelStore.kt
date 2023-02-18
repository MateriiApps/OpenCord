package com.xinto.opencord.store

import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.db.entity.channel.toEntity
import com.xinto.opencord.domain.channel.DomainChannel
import com.xinto.opencord.domain.channel.toDomain
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.ChannelCreateEvent
import com.xinto.opencord.gateway.event.ChannelDeleteEvent
import com.xinto.opencord.gateway.event.ChannelUpdateEvent
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.withContext

typealias ChannelEvent = Event<DomainChannel, DomainChannel, Long>

interface ChannelStore {
    fun observeChannel(channelId: Long): Flow<ChannelEvent>
    fun observeChannels(guildId: Long): Flow<ChannelEvent>

    suspend fun fetchChannel(channelId: Long): DomainChannel?
    suspend fun fetchChannels(guildId: Long): List<DomainChannel>
}

class ChannelStoreImpl(
    gateway: DiscordGateway,
    private val cache: CacheDatabase,
) : ChannelStore {
    private val events = MutableSharedFlow<ChannelEvent>()

    override fun observeChannel(channelId: Long): Flow<ChannelEvent> {
        return events.filter { event ->
            event.fold(
                onAdd = { it.id == channelId },
                onUpdate = { it.id == channelId },
                onDelete = { it == channelId },
            )
        }
    }

    override fun observeChannels(guildId: Long): Flow<ChannelEvent> {
        return events.filter { event ->
            event.fold(
                onAdd = { it.guildId == guildId },
                onUpdate = { it.guildId == guildId },
                onDelete = { it == guildId },
            )
        }
    }

    override suspend fun fetchChannel(channelId: Long): DomainChannel? {
        return withContext(Dispatchers.IO) {
            cache.channels().getChannel(channelId)?.toDomain()
        }
    }

    override suspend fun fetchChannels(guildId: Long): List<DomainChannel> {
        return withContext(Dispatchers.IO) {
            cache.channels().getChannels(guildId)
                .map { it.toDomain() }
        }
    }

    init {
        gateway.onEvent<ReadyEvent> { event ->
            val channels = event.data.guilds.flatMap { guild ->
                guild.channels.map { it.toEntity(guild.id.value) }
            }

            for (channel in channels) {
                events.emit(ChannelEvent.Add(channel.toDomain()))
            }

            cache.channels().replaceAllChannels(channels)
        }

        gateway.onEvent<ChannelCreateEvent> {
            val guildId = it.data.guildId?.value
                ?: error("no guild id on channel create event")

            events.emit(ChannelEvent.Add(it.data.toDomain()))
            cache.channels().insertChannel(it.data.toEntity(guildId))
        }

        gateway.onEvent<ChannelUpdateEvent> {
            val guildId = it.data.guildId?.value
                ?: error("no guild id on channel update event")

            events.emit(ChannelEvent.Update(it.data.toDomain()))
            cache.channels().insertChannel(it.data.toEntity(guildId))
        }

        gateway.onEvent<ChannelDeleteEvent> {
            val channelId = it.data.id.value

            events.emit(ChannelEvent.Delete(channelId))

            cache.runInTransaction {
                cache.channels().deleteChannel(channelId)
                cache.messages().deleteByChannel(channelId)
            }
        }
    }
}
