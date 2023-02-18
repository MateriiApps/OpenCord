package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.channel.EntityChannel
import kotlinx.coroutines.delay

@Dao
interface ChannelsDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityChannel::class,
    )
    fun insertChannel(channel: EntityChannel)

    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityChannel::class,
    )
    fun insertChannels(channels: List<EntityChannel>)

    suspend fun replaceAllChannels(channels: List<EntityChannel>) {
        clear()
        delay(100) // FIXME: RoomDB not preserving query order
        insertChannels(channels)
    }

    // --------------- Updates ---------------
    @Query("UPDATE channels SET last_message_id = :lastMessageId WHERE id = :channelId")
    fun setLastMessageId(channelId: Long, lastMessageId: Long)

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

    @Query("SELECT is_pins_stored FROM channels WHERE id = :channelId LIMIT 1")
    fun isChannelPinsStored(channelId: Long): Boolean?

    @Query("SELECT last_message_id FROM channels WHERE id = :channelId LIMIT 1")
    fun getLastMessageId(channelId: Long): Long?
}
