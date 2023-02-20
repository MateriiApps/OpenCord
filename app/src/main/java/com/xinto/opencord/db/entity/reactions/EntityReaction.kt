package com.xinto.opencord.db.entity.reactions

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.github.materiiapps.partial.getOrElse
import com.xinto.opencord.rest.models.reaction.ApiReaction

@Entity(
    tableName = "reactions",
    primaryKeys = ["message_id", "emoji_name", "emoji_id"],
)
data class EntityReaction(
    @ColumnInfo(name = "message_id")
    val messageId: Long,

    @ColumnInfo(name = "message_guild_id")
    val messageGuildId: Long? = null,

    @ColumnInfo(name = "emoji_name")
    val emojiName: String = "", // empty is null

    @ColumnInfo(name = "emoji_id")
    val emojiId: Long = 0, // 0 is null

    @ColumnInfo(name = "guild_emoji_animated")
    val animated: Boolean = false,

    @ColumnInfo(name = "count")
    val count: Int = 0,

    @ColumnInfo(name = "me_reacted")
    val meReacted: Boolean = false,

    @ColumnInfo("reaction_created")
    val reactionCreated: Long? = System.currentTimeMillis()
)

fun ApiReaction.toEntity(messageId: Long, messageGuildId: Long?, reactionCreated: Long? = System.currentTimeMillis()): EntityReaction {
    return EntityReaction(
        messageId = messageId,
        messageGuildId = messageGuildId,
        emojiName = emoji.name ?: "",
        emojiId = emoji.id?.value ?: 0,
        animated = emoji.animated.getOrElse { false },
        count = count,
        meReacted = meReacted,
        reactionCreated = reactionCreated,
    )
}
