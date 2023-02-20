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

    // --------------- Deletes ---------------
    @Query("UPDATE reactions SET count = count + 1 WHERE message_id = :messageId AND emoji_id = :emojiId AND emoji_name = :emojiName")
    fun incrementCount(messageId: Long, emojiId: Long?, emojiName: String?)

    @Query("UPDATE reactions SET count = count - 1 WHERE message_id = :messageId AND emoji_id = :emojiId AND emoji_name = :emojiName AND count > 0")
    fun decrementCount(messageId: Long, emojiId: Long?, emojiName: String?)

    @Query("UPDATE reactions SET me_reacted = :meReacted WHERE message_id = :messageId AND emoji_id = :emojiId AND emoji_name = :emojiName")
    fun setMeReacted(messageId: Long, emojiId: Long?, emojiName: String?, meReacted: Boolean)

    // --------------- Deletes ---------------
    @Query("DELETE FROM reactions WHERE message_id = :messageId")
    fun deleteByMessage(messageId: Long)

//    @Query("DELETE FROM reactions WHERE message_guild_id = :guildId")
//    fun deleteByGuild(guildId: Long)

    @Query("DELETE FROM reactions")
    fun clear()

    // --------------- Queries ---------------
    @Query("SELECT * FROM reactions WHERE message_id = :messageId")
    fun getReactions(messageId: Long): List<EntityReaction>
}
