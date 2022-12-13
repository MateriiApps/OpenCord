package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.xinto.opencord.db.entity.guild.EntityGuild
import kotlinx.coroutines.flow.Flow

@Dao
interface GuildsDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
    )
    suspend fun insertGuild(guild: EntityGuild)

    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityGuild::class,
    )
    suspend fun insertGuilds(guilds: List<EntityGuild>)

    // --------------- Updates ---------------
    @Update
    suspend fun updateGuild(guild: EntityGuild)

    // --------------- Deletes ---------------
    @Query("DELETE FROM guilds WHERE id = :guildId")
    suspend fun deleteGuildById(guildId: Long)

    @Query("DELETE FROM guilds")
    suspend fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * FROM guilds WHERE id = :guildId LIMIT 1")
    suspend fun getGuildById(guildId: Long): EntityGuild?

    @Query("SELECT * FROM guilds WHERE id IN(:guildIds)")
    suspend fun getGuildsByIds(guildIds: List<Long>): List<EntityGuild>

    // -------------- Observables -------------
    @Query("SELECT * FROM guilds")
    fun observeGuilds(): Flow<List<EntityGuild>>

    @Query("SELECT * FROM guilds WHERE id = :guildId LIMIT 1")
    fun observeGuild(guildId: Long): Flow<EntityGuild?>
}
