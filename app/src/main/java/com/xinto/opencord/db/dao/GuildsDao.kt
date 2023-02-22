package com.xinto.opencord.db.dao

import androidx.room.*
import com.xinto.opencord.db.entity.guild.EntityGuild

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

    @Transaction
    fun replaceAllGuilds(guilds: List<EntityGuild>) {
        clear()
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

    @Query("SELECT * FROM guilds")
    fun getGuilds(): List<EntityGuild>
}
