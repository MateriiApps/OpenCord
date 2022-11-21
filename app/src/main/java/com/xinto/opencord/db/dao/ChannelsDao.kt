package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.channel.EntityChannel

@Dao
interface ChannelsDao {
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityChannel::class,
    )
    fun insertChannels(vararg channels: EntityChannel)

    @Query("DELETE FROM channels WHERE id IN(:channelIds)")
    fun deleteChannels(vararg channelIds: Long)

    @Query("DELETE FROM channels WHERE guild_id = :guildId AND id NOT IN(:knownIds)")
    fun deleteUnknownChannels(guildId: Long, vararg knownIds: Long)

    @Query("SELECT * FROM channels WHERE id = :channelId LIMIT 1")
    fun getChannel(channelId: Long): EntityChannel?

    @Query("SELECT * FROM channels WHERE guild_id = :guildId")
    fun getChannels(guildId: Long): List<EntityChannel>
}
