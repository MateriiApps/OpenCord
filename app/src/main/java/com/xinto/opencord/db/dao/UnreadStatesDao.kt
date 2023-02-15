package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.channel.EntityUnreadState

@Dao
interface UnreadStatesDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityUnreadState::class,
    )
    fun insertUnreadStates(states: List<EntityUnreadState>)

    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityUnreadState::class,
    )
    fun insertUnreadState(states: EntityUnreadState)

    // --------------- Deletes ---------------
    @Query("DELETE FROM unread_states WHERE channel_id = :channelId")
    fun deleteUnreadState(channelId: Long)

    @Query("DELETE FROM channels")
    fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * from unread_states WHERE channel_id = :channelId LIMIT 1")
    fun getUnreadState(channelId: Long): EntityUnreadState?
}
