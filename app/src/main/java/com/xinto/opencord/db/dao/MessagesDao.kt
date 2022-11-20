package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.message.EntityAttachment
import com.xinto.opencord.db.entity.message.EntityEmbed
import com.xinto.opencord.db.entity.message.EntityMessage

@Dao
interface MessagesDao {
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityMessage::class,
    )
    fun insertMessages(vararg message: EntityMessage)

    @Query("DELETE FROM messages WHERE id IN(:messageIds)")
    fun deleteMessages(vararg messageIds: Long)

    @Query("SELECT * FROM messages WHERE id = :id LIMIT 1")
    fun getMessage(id: Long): EntityMessage

    @Query("SELECT * FROM messages WHERE channel_id = :channelId ORDER BY id DESC LIMIT :limit")
    fun getMessagesLast(channelId: Long, limit: Int): List<EntityMessage>

    @Query("SELECT * FROM messages WHERE channel_id = :channelId AND id < :beforeId ORDER BY id DESC LIMIT :limit")
    fun getMessagesBefore(channelId: Long, limit: Int, beforeId: Long): List<EntityMessage>

    @Query("SELECT * FROM messages WHERE channel_id = :channelId AND id > :afterId ORDER BY id ASC LIMIT :limit")
    fun getMessagesAfter(channelId: Long, limit: Int, afterId: Long): List<EntityMessage>

    @Query("SELECT * FROM messages WHERE channel_id = :channelId AND id >= :aroundId - ROUND(:limit / 2, 0) ORDER BY id ASC LIMIT :limit")
    fun getMessagesAround(channelId: Long, limit: Int, aroundId: Long): List<EntityMessage>

    @Query("SELECT * FROM attachments WHERE id = :messageId")
    fun getAttachments(messageId: Long): List<EntityAttachment>

    @Query("SELECT * FROM embeds WHERE message_id = :messageId ORDER BY embed_index ASC")
    fun getEmbeds(messageId: Long): List<EntityEmbed>
}