package com.xinto.opencord.store

import androidx.room.withTransaction
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface ChannelStore {
    fun observeChannel(id: Long): Flow<DomainChannel?>
    fun observeChannels(guildId: Long): Flow<List<DomainChannel>>

    suspend fun fetchChannel(channelId: Long): DomainChannel?
    suspend fun fetchChannels(guildId: Long): List<DomainChannel>
}

class ChannelStoreImpl(
    gateway: DiscordGateway,
    private val cache: CacheDatabase,
) : ChannelStore {
    private val channels = cache.channels()
    private val messages = cache.messages()

    override fun observeChannel(id: Long): Flow<DomainChannel?> {
        return channels.observeChannel(id)
            .map { it?.toDomain() }
    }

    override fun observeChannels(guildId: Long): Flow<List<DomainChannel>> {
        return channels.observeChannels(guildId)
            .map {
                it.map {
                    it.toDomain()
                }
            }
    }

    override suspend fun fetchChannel(channelId: Long): DomainChannel? {
        return withContext(Dispatchers.IO) {
            channels.getChannel(channelId)?.toDomain()
        }
    }

    override suspend fun fetchChannels(guildId: Long): List<DomainChannel> {
        return withContext(Dispatchers.IO) {
            channels.getChannels(guildId)
                .map { it.toDomain() }
        }
    }

    init {
        gateway.onEvent<ReadyEvent> { event ->
            val channels = event.data.guilds.flatMap { guild ->
                guild.channels.map { it.toEntity(guild.id.value) }
            }

            cache.withTransaction {
                this.channels.apply {
                    clear()
                    insertChannels(channels)
                }
            }
        }
        gateway.onEvent<ChannelCreateEvent> {
            val guildId = it.data.guildId?.value
                ?: error("no guild id on channel create event")

            channels.insertChannel(it.data.toEntity(guildId))
        }

        gateway.onEvent<ChannelUpdateEvent> {
            val guildId = it.data.guildId?.value
                ?: error("no guild id on channel update event")

            channels.updateChannel(it.data.toEntity(guildId))
        }

        gateway.onEvent<ChannelDeleteEvent> {
            val channelId = it.data.id.value

            cache.withTransaction {
                channels.deleteChannel(channelId)
                messages.deleteByChannel(channelId)
            }
        }
    }
}
