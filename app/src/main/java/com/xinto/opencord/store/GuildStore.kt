package com.xinto.opencord.store

import androidx.room.withTransaction
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.db.entity.guild.toEntity
import com.xinto.opencord.domain.guild.DomainGuild
import com.xinto.opencord.domain.guild.toDomain
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.GuildCreateEvent
import com.xinto.opencord.gateway.event.GuildDeleteEvent
import com.xinto.opencord.gateway.event.GuildUpdateEvent
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

interface GuildStore {
    fun observeGuild(guildId: Long): Flow<DomainGuild?>
    fun observeGuilds(): Flow<List<DomainGuild>>

    suspend fun fetchGuild(guildId: Long): DomainGuild?
}

class GuildStoreImpl(
    gateway: DiscordGateway,
    private val cache: CacheDatabase,
) : GuildStore {
    private val guilds = cache.guilds()
    private val channels = cache.channels()

    override fun observeGuild(guildId: Long): Flow<DomainGuild?> {
        return guilds.observeGuild(guildId)
            .map { it?.toDomain() }
    }

    override fun observeGuilds() = guilds
        .observeGuilds()
        .map {
            it.map {
                it.toDomain()
            }
        }

    override suspend fun fetchGuild(guildId: Long): DomainGuild? {
        return withContext(Dispatchers.IO) {
            cache.guilds().getGuildById(guildId)?.toDomain()
        }
    }

    init {
        gateway.onEvent<ReadyEvent> { event ->
            val guilds = event.data.guilds.map { it.toEntity() }

            cache.withTransaction {
                this.guilds.apply {
                    clear()
                    insertGuilds(guilds)
                }
            }
        }

        gateway.onEvent<GuildCreateEvent> {
            guilds.insertGuild(it.data.toEntity())
        }

        gateway.onEvent<GuildUpdateEvent> {
            guilds.updateGuild(it.data.toEntity())
        }

        gateway.onEvent<GuildDeleteEvent> {
            val guildId = it.data.id.value

            cache.withTransaction {
                guilds.deleteGuildById(guildId)
                channels.deleteChannelsByGuild(guildId)
            }
        }
    }
}
