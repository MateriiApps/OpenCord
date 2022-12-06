package com.xinto.opencord.store

import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.mapper.toEntity
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.GuildCreateEvent
import com.xinto.opencord.gateway.event.GuildDeleteEvent
import com.xinto.opencord.gateway.event.GuildUpdateEvent
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.withContext

interface GuildStore {
    fun observeGuild(guildId: Long): Flow<Event<DomainGuild>>
    fun observeGuilds(): Flow<Event<DomainGuild>>

    suspend fun fetchGuild(guildId: Long): DomainGuild?
}

class GuildStoreImpl(
    gateway: DiscordGateway,
    private val cache: CacheDatabase,
) : GuildStore {
    private val events = MutableSharedFlow<Event<DomainGuild>>()

    override fun observeGuild(guildId: Long): Flow<Event<DomainGuild>> {
        return events.filter { event ->
            event.fold(
                onAdd = { it.id == guildId },
                onUpdate = { it.id == guildId },
                onRemove = { it == guildId }
            )
        }
    }

    override fun observeGuilds() = events

    override suspend fun fetchGuild(guildId: Long): DomainGuild? {
        return withContext(Dispatchers.IO) {
            cache.guilds().getGuild(guildId)?.toDomain()
        }
    }

    init {
        gateway.onEvent<ReadyEvent> { event ->
            val guilds = event.data.guilds.map { it.toEntity() }

            for (guild in guilds) {
                events.emit(Event.Add(guild.toDomain()))
            }

            cache.runInTransaction {
                cache.guilds().apply {
                    clear()
                    insertGuilds(guilds)
                }
            }
        }

        gateway.onEvent<GuildCreateEvent> {
            events.emit(Event.Add(it.data.toDomain()))

            cache.guilds().insertGuilds(listOf(it.data.toEntity()))
        }

        gateway.onEvent<GuildUpdateEvent> {
            events.emit(Event.Update(it.data.toDomain()))
            cache.guilds().insertGuilds(listOf(it.data.toEntity()))
        }

        gateway.onEvent<GuildDeleteEvent> {
            val guildId = it.data.id.value

            events.emit(Event.Remove(guildId))

            cache.runInTransaction {
                cache.guilds().deleteGuild(guildId)
                cache.channels().deleteChannelsByGuild(guildId)
            }
        }
    }
}
