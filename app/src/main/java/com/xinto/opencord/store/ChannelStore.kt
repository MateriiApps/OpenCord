package com.xinto.opencord.store

import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.mapper.toEntity
import com.xinto.opencord.domain.model.DomainChannel
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

interface ChannelStore {
    fun observeChannel(channelId: Long): Flow<Event<DomainChannel>>
    fun observeChannels(guildId: Long): Flow<Event<DomainChannel>>

    suspend fun fetchChannel(channelId: Long): DomainChannel?
    suspend fun fetchChannels(guildId: Long): List<DomainChannel>
}

class ChannelStoreImpl(
    gateway: DiscordGateway,
    private val cache: CacheDatabase,
) : ChannelStore {
    private val events = MutableSharedFlow<Event<DomainChannel>>()

    override fun observeChannel(channelId: Long): Flow<Event<DomainChannel>> {
        return events.filter { event ->
            event.fold(
                onAdd = { it.id == channelId },
                onUpdate = { it.id == channelId },
                onRemove = { it == channelId },
            )
        }
    }

    override fun observeChannels(guildId: Long): Flow<Event<DomainChannel>> {
        return events.filter { event ->
            event.fold(
                onAdd = { it.guildId == guildId },
                onUpdate = { it.guildId == guildId },
                onRemove = { it == guildId },
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
                events.emit(Event.Add(channel.toDomain()))
            }

            cache.runInTransaction {
                cache.channels().apply {
                    clear()
                    insertChannels(channels)
                }
            }
        }

        gateway.onEvent<ChannelCreateEvent> {
            val guildId = it.data.guildId?.value
                ?: error("no guild id on channel create event")

            events.emit(Event.Add(it.data.toDomain()))
            cache.channels().insertChannels(listOf(it.data.toEntity(guildId)))
        }

        gateway.onEvent<ChannelUpdateEvent> {
            val guildId = it.data.guildId?.value
                ?: error("no guild id on channel update event")

            events.emit(Event.Update(it.data.toDomain()))
            cache.channels().insertChannels(listOf(it.data.toEntity(guildId)))
        }

        gateway.onEvent<ChannelDeleteEvent> {
            val channelId = it.data.id.value

            events.emit(Event.Remove(channelId))

            cache.runInTransaction {
                cache.channels().deleteChannel(channelId)
                cache.messages().deleteByChannel(channelId)
            }
        }
    }
}
