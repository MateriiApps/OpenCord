package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.channel.EntityLastMessageId

@Dao
interface LastMessageIdsDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityLastMessageId::class,
    )
    fun setLastMessageId(data: EntityLastMessageId)

    // --------------- Deletes ---------------
    @Query("DELETE FROM channels")
    fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * FROM last_message_ids WHERE channel_id = :channelId LIMIT 1")
    fun getLastMessageId(channelId: Long): EntityLastMessageId?
}
