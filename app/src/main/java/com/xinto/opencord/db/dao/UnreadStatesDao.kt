package com.xinto.opencord.db.dao

import androidx.room.*
import com.xinto.opencord.db.entity.channel.EntityUnreadState

@Dao
interface UnreadStatesDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityUnreadState::class,
    )
    fun insertState(state: EntityUnreadState)

    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityUnreadState::class,
    )
    fun insertStates(states: List<EntityUnreadState>)

    @Transaction
    fun replaceAllStates(states: List<EntityUnreadState>) {
        clear()
        insertStates(states)
    }

    // --------------- Updates ---------------
    @Query("UPDATE unread_states SET mention_count = coalesce(mention_count, 0) + 1 WHERE channel_id = :channelId")
    fun incrementMentionCount(channelId: Long)

    // --------------- Deletes ---------------
    @Query("DELETE FROM unread_states WHERE channel_id = :channelId")
    fun deleteUnreadState(channelId: Long)

    @Query("DELETE FROM unread_states")
    fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * from unread_states WHERE channel_id = :channelId LIMIT 1")
    fun getUnreadState(channelId: Long): EntityUnreadState?
}
