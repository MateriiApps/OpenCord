package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.guild.EntityGuild
import kotlinx.coroutines.delay

@Dao
interface GuildsDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityGuild::class,
    )
    fun insertGuild(guild: EntityGuild)

    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityGuild::class,
    )
    fun insertGuilds(guilds: List<EntityGuild>)

    suspend fun replaceAllGuilds(guilds: List<EntityGuild>) {
        clear()
        delay(100) // FIXME: RoomDB not preserving query order
        insertGuilds(guilds)
    }

    // --------------- Deletes ---------------
    @Query("DELETE FROM guilds WHERE id = :guildId")
    fun deleteGuild(guildId: Long)

    @Query("DELETE FROM guilds")
    fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * FROM guilds WHERE id = :guildId LIMIT 1")
    fun getGuild(guildId: Long): EntityGuild?

    @Query("SELECT * FROM guilds WHERE id IN(:guildIds)")
    fun getGuilds(guildIds: List<Long>): List<EntityGuild>
}
