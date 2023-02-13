package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.channel.EntityChannel

@Dao
interface ChannelsDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityChannel::class,
    )
    fun insertChannels(channels: List<EntityChannel>)

    @Query("UPDATE channels SET is_pins_stored = :isStored WHERE id = :channelId")
    fun setChannelPinsStored(channelId: Long, isStored: Boolean = true)

    // --------------- Deletes ---------------
    @Query("DELETE FROM channels WHERE id = :channelId")
    fun deleteChannel(channelId: Long)

    @Query("DELETE FROM channels WHERE guild_id = :guildId")
    fun deleteChannelsByGuild(guildId: Long)

    @Query("DELETE FROM channels")
    fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * FROM channels WHERE id = :channelId LIMIT 1")
    fun getChannel(channelId: Long): EntityChannel?

    @Query("SELECT * FROM channels WHERE guild_id = :guildId")
    fun getChannels(guildId: Long): List<EntityChannel>

    @Query("SELECT is_pins_stored FROM channels WHERE id = :channelId")
    fun isChannelPinsStored(channelId: Long): Boolean?
}
