package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.message.EntityMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
    )
    suspend fun insertMessage(messages: EntityMessage)

    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityMessage::class,
    )
    suspend fun insertMessages(messages: List<EntityMessage>)

    // --------------- Deletes ---------------
    @Query("DELETE FROM messages WHERE id = :messageId")
    suspend fun deleteMessage(messageId: Long)

    @Query("DELETE FROM messages WHERE channel_id = :channelId")
    suspend fun deleteByChannel(channelId: Long)

    @Query("DELETE FROM messages")
    suspend fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * FROM messages WHERE id = :id LIMIT 1")
    suspend fun getMessage(id: Long): EntityMessage?

    @Query("SELECT * FROM messages WHERE channel_id = :channelId ORDER BY id DESC LIMIT :limit")
    suspend fun getMessagesLast(channelId: Long, limit: Int): List<EntityMessage>

    @Query("SELECT * FROM messages WHERE channel_id = :channelId AND id < :beforeId ORDER BY id DESC LIMIT :limit")
    suspend fun getMessagesBefore(channelId: Long, limit: Int, beforeId: Long): List<EntityMessage>

    @Query("SELECT * FROM messages WHERE channel_id = :channelId AND id > :afterId ORDER BY id ASC LIMIT :limit")
    suspend fun getMessagesAfter(channelId: Long, limit: Int, afterId: Long): List<EntityMessage>

    @Query("SELECT * FROM messages WHERE channel_id = :channelId AND id >= :aroundId - ROUND(:limit / 2, 0) ORDER BY id ASC LIMIT :limit")
    suspend fun getMessagesAround(channelId: Long, limit: Int, aroundId: Long): List<EntityMessage>

    @Query("SELECT * FROM messages WHERE pinned = 1 AND channel_id = :channelId")
    suspend fun getPinnedMessages(channelId: Long): List<EntityMessage>

    // -------------- Observables -------------
    @Query("SELECT * FROM messages WHERE id = :id LIMIT 1")
    fun observeMessage(id: Long): Flow<EntityMessage?>

    @Query("SELECT * FROM messages WHERE channel_id = :channelId")
    fun observeMessagesByChannelId(channelId: Long): Flow<List<EntityMessage>>
}
