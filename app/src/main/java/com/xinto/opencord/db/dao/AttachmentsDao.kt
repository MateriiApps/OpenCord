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
        entity = EntityAttachment::class,
    )
    fun insertAttachments(attachments: List<EntityAttachment>)

    // --------------- Deletes ---------------
    @Query("DELETE FROM attachments WHERE message_id = :messageId")
    fun deleteAttachments(messageId: Long)

    @Query("DELETE FROM attachments")
    fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * FROM attachments WHERE id = :messageId")
    fun getAttachments(messageId: Long): List<EntityAttachment>
}
