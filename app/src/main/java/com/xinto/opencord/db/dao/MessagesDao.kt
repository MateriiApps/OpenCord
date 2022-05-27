package com.xinto.opencord.db.dao

import androidx.room.*
import com.xinto.opencord.db.entity.EntityAttachment
import com.xinto.opencord.db.entity.EntityMessage
import com.xinto.opencord.db.entity.EntityMessageFull

@Dao
abstract class MessagesDao {

    @Transaction
    @Query("SELECT * FROM messages")
    abstract suspend fun getAllMessages(): List<EntityMessageFull>

    @Transaction
    @Query("SELECT * FROM messages WHERE channel_id = :channelId")
    abstract suspend fun getMessagesByChannelId(channelId: Long): List<EntityMessageFull>

    @Transaction
    @Query("SELECT * FROM messages WHERE message_id = :id LIMIT 1")
    abstract suspend fun getMessageById(id: Long): EntityMessageFull?

    @Transaction
    open suspend fun insert(message: EntityMessageFull) {
        insertMessage(message.message)
        message.attachments.forEach {
            it.messageId = message.message.id
            insertAttachment(it)
        }
    }

    @Transaction
    open suspend fun insertAll(messages: List<EntityMessageFull>) {
        messages.forEach {
            insert(it)
        }
    }

    @Transaction
    open suspend fun update(message: EntityMessageFull) {
        updateMessage(message.message)
        message.attachments.forEach {
            updateAttachment(it)
        }
    }

    @Transaction
    open suspend fun delete(message: EntityMessageFull) {
        deleteMessage(message.message)
        message.attachments.forEach {
            deleteAttachment(it)
        }
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    protected abstract suspend fun insertMessage(entityMessage: EntityMessage)

    @Update
    protected abstract suspend fun updateMessage(entityMessage: EntityMessage)

    @Delete
    protected abstract suspend fun deleteMessage(entityMessage: EntityMessage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertAttachment(entityAttachment: EntityAttachment)

    @Update
    protected abstract suspend fun updateAttachment(entityAttachment: EntityAttachment)

    @Delete
    protected abstract suspend fun deleteAttachment(attachment: EntityAttachment)

}