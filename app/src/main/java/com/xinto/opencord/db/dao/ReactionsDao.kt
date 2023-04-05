package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.reactions.EntityReaction

@Dao
interface ReactionsDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityReaction::class,
    )
    fun insertReactions(reactions: List<EntityReaction>)

    // --------------- Updates ---------------
    @Query(
        "UPDATE reactions SET " +
                "count = max(0, count + :countDiff), " +
                "me_reacted = coalesce(:meReacted, me_reacted), " +
                "reaction_created = CASE " +
                "  WHEN (count + :countDiff <= 0) THEN NULL " +
                "  WHEN (reaction_created == NULL) THEN :updateTime " +
                "  ELSE reaction_created " +
                "END " +
                "WHERE message_id = :messageId " +
                "AND emoji_id = :emojiId " +
                "AND emoji_name = :emojiName",
    )
    fun updateReaction(
        messageId: Long,
        emojiId: Long?,
        emojiName: String?,
        meReacted: Boolean?,
        countDiff: Int,
        updateTime: Long? = System.currentTimeMillis(),
    )

    // --------------- Deletes ---------------
    @Query("DELETE FROM reactions WHERE message_id = :messageId")
    fun deleteByMessage(messageId: Long)

//    @Query("DELETE FROM reactions WHERE message_guild_id = :guildId")
//    fun deleteByGuild(guildId: Long)

    @Query("DELETE FROM reactions")
    fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * FROM reactions WHERE message_id = :messageId AND count > 0")
    fun getReactions(messageId: Long): List<EntityReaction>
}
