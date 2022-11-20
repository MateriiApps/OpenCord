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

    suspend fun fetchChannel(channelId: Long): DomainChannel
    suspend fun fetchChannels(guildId: Long): List<DomainChannel>
}

class ChannelStoreImpl(
    gateway: DiscordGateway,
    private val cache: CacheDatabase,
) : ChannelStore {
    private val events = MutableSharedFlow<Event<DomainChannel>>()

    override fun observeChannel(channelId: Long): Flow<Event<DomainChannel>> {
        return events.filter {
            it.data?.id == channelId
        }
    }

    override fun observeChannels(guildId: Long): Flow<Event<DomainChannel>> {
        return events.filter {
            it.data?.guildId == guildId
        }
    }

    override suspend fun fetchChannel(channelId: Long): DomainChannel {
        return withContext(Dispatchers.IO) {
            cache.channels().getChannel(channelId)
                .toDomain()
        }
    }

    override suspend fun fetchChannels(guildId: Long): List<DomainChannel> {
        return withContext(Dispatchers.IO) {
            cache.channels().getChannels(guildId)
                .map { it.toDomain() }
        }
    }

    init {
        gateway.onEvent<ReadyEvent> {
            // TODO: replace channels here
        }

        gateway.onEvent<ChannelCreateEvent> {
            events.emit(Event.Add(it.data.toDomain()))
            cache.channels().insertChannels(it.data.toEntity())
        }

        gateway.onEvent<ChannelUpdateEvent> {
            // TODO: logic for channel update
        }

        gateway.onEvent<ChannelDeleteEvent> {
            events.emit(Event.Remove(it.data.id.value))
            cache.channels().deleteChannels(it.data.id.value)
        }
    }
}
