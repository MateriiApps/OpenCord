package com.xinto.opencord.store

import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.mapper.toEntity
import com.xinto.opencord.domain.model.DomainGuild
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.*
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
        return events.filter {
            it.data?.id == guildId
        }
    }

    override fun observeGuilds(): Flow<Event<DomainGuild>> {
        return events
    }

    override suspend fun fetchGuild(guildId: Long): DomainGuild? {
        return withContext(Dispatchers.IO) {
            cache.guilds().getGuild(guildId)?.toDomain()
        }
    }

    init {
        gateway.onEvent<ReadyEvent> { event ->
            val guilds = event.data.guilds.map { it.toEntity() }

            cache.guilds().apply {
                insertGuilds(*guilds.toTypedArray())
                deleteUnknownGuilds(*guilds.map { it.id }.toLongArray())
            }
        }

        gateway.onEvent<GuildCreateEvent> {
            events.emit(Event.Add(it.data.toDomain()))
            cache.guilds().insertGuilds(it.data.toEntity())
        }

        gateway.onEvent<GuildUpdateEvent> {
            // TODO: logic for guild update
        }

        gateway.onEvent<GuildDeleteEvent> {
            events.emit(Event.Remove(it.data.id.value))
            cache.guilds().deleteGuild(it.data.id.value)
        }
    }
}
