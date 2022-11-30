package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.message.EntityEmbed

@Dao
interface EmbedsDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityEmbed::class
    )
    fun insertEmbeds(embeds: List<EntityEmbed>)

    // --------------- Deletes ---------------
    @Query("DELETE FROM embeds WHERE message_id = :messageId")
    fun deleteEmbeds(messageId: Long)

    @Query("DELETE FROM embeds WHERE message_id = :messageId AND embed_index >= :embedCount")
    fun deleteTrailingEmbeds(messageId: Long, embedCount: Int)

    @Query("DELETE FROM embeds")
    fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * FROM embeds WHERE message_id = :messageId ORDER BY embed_index ASC")
    fun getEmbeds(messageId: Long): List<EntityEmbed>
}
