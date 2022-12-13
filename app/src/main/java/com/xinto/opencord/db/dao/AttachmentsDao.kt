package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.message.EntityAttachment

@Dao
interface AttachmentsDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
    )
    suspend fun insertAttachment(attachment: EntityAttachment)

    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityAttachment::class,
    )
    suspend fun insertAttachments(attachments: List<EntityAttachment>)

    // --------------- Deletes ---------------
    @Query("DELETE FROM attachments WHERE message_id = :messageId")
    suspend fun deleteAttachmentsByMessageId(messageId: Long)

    @Query("DELETE FROM attachments")
    suspend fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * FROM attachments WHERE id = :messageId")
    suspend fun getAttachmentsByMessageId(messageId: Long): List<EntityAttachment>
}
