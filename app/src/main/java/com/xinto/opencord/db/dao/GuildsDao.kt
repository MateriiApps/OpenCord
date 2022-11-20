package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.guild.EntityGuild

@Dao
interface GuildsDao {
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityGuild::class,
    )
    fun insertGuilds(vararg guilds: EntityGuild)

    @Query("DELETE FROM guilds WHERE id = :guildId")
    fun deleteGuild(guildId: Long)

    @Query("DELETE FROM guilds WHERE id NOT IN(:knownIds)")
    fun deleteUnknownGuilds(vararg knownIds: Long)

    @Query("SELECT * FROM guilds WHERE id = :guildId LIMIT 1")
    fun getGuild(guildId: Long): EntityGuild

    @Query("SELECT * FROM guilds WHERE id IN(:guildIds)")
    fun getGuilds(guildIds: List<Long>): List<EntityGuild>
}