package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.xinto.opencord.db.entity.channel.EntityChannel
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelsDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
    )
    suspend fun insertChannel(channel: EntityChannel)

    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityChannel::class,
    )
    suspend fun insertChannels(channels: List<EntityChannel>)

    // --------------- Updates ---------------
    @Update
    suspend fun updateChannel(channel: EntityChannel)

    // --------------- Inserts ---------------
    @Query("UPDATE channels SET is_pins_stored = :isStored WHERE id = :channelId")
    suspend fun setChannelPinsStored(channelId: Long, isStored: Boolean = true)

    // --------------- Deletes ---------------
    @Query("DELETE FROM channels WHERE id = :channelId")
    suspend fun deleteChannel(channelId: Long)

    @Query("DELETE FROM channels WHERE guild_id = :guildId")
    suspend fun deleteChannelsByGuild(guildId: Long)

    @Query("DELETE FROM channels")
    suspend fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * FROM channels WHERE id = :channelId LIMIT 1")
    suspend fun getChannel(channelId: Long): EntityChannel?

    @Query("SELECT * FROM channels WHERE guild_id = :guildId")
    suspend fun getChannels(guildId: Long): List<EntityChannel>

    @Query("SELECT is_pins_stored FROM channels WHERE id = :channelId")
    suspend fun isChannelPinsStored(channelId: Long): Boolean?

    // --------------- Observables ------------
    @Query("SELECT * FROM channels WHERE id = :channelId LIMIT 1")
    fun observeChannel(channelId: Long): Flow<EntityChannel?>

    @Query("SELECT * FROM channels WHERE guild_id = :guildId")
    fun observeChannels(guildId: Long): Flow<List<EntityChannel>>

}
