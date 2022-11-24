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

    // --------------- Deletes ---------------
    @Query("DELETE FROM channels WHERE id = :channelId")
    fun deleteChannel(channelId: Long)

    @Query("DELETE FROM channels")
    fun deleteAllChannels()

    @Query("DELETE FROM channels WHERE guild_id = guild_id")
    fun deleteChannelsByGuild(guildId: Long)

    // --------------- Queries ---------------
    @Query("SELECT * FROM channels WHERE id = :channelId LIMIT 1")
    fun getChannel(channelId: Long): EntityChannel?

    @Query("SELECT * FROM channels WHERE guild_id = :guildId")
    fun getChannels(guildId: Long): List<EntityChannel>
}
