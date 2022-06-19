package com.xinto.opencord.db.dao

import androidx.room.*
import com.xinto.opencord.db.entity.EntityChannel

@Dao
abstract class ChannelsDao {

    @Transaction
    @Query("SELECT * FROM channels")
    abstract suspend fun getAll(): List<EntityChannel>

    @Transaction
    @Query("SELECT * FROM channels WHERE channel_guild_id = :guildId")
    abstract suspend fun getAllByGuildId(guildId: Long): List<EntityChannel>

    @Transaction
    @Query("SELECT * FROM channels WHERE channel_id = :id")
    abstract suspend fun getById(id: Long): EntityChannel?

    @Insert
    abstract suspend fun insert(channel: EntityChannel)

    @Insert
    abstract suspend fun insertAll(channel: List<EntityChannel>)

    @Update
    abstract suspend fun update(channel: EntityChannel)

    @Delete
    abstract suspend fun delete(channel: EntityChannel)

}