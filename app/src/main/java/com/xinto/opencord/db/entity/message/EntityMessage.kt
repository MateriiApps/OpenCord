package com.xinto.opencord.db.entity.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.xinto.opencord.rest.models.message.ApiMessage

@Entity(
    tableName = "messages",
    indices = [
        Index(value = ["channel_id"]),
        Index(value = ["author_id"]),
    ],
)
data class EntityMessage(
    // -------- Discord data -------- //
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "channel_id")
    val channelId: Long,

    @ColumnInfo(name = "type")
    val type: Int,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "pinned")
    val pinned: Boolean,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "author_id")
    val authorId: Long,

    @ColumnInfo(name = "edited_timestamp")
    val editedTimestamp: Long?,

    @ColumnInfo(name = "referenced_message")
    val referencedMessageId: Long?,

    @ColumnInfo(name = "mentions_everyone")
    val mentionsEveryone: Boolean,

    // TODO: add user mentions
    // TODO: add role mentions

    // -------- OpenCord data -------- //
//    @ColumnInfo(name = "deleted")
//    val deleted: Boolean,

    // -------- DB relational data -------- //
    @ColumnInfo(name = "has_attachments")
    val hasAttachments: Boolean,

    @ColumnInfo(name = "has_embeds")
    val hasEmbeds: Boolean,
)

fun ApiMessage.toEntity(): EntityMessage {
    return EntityMessage(
        id = id.value,
        channelId = channelId.value,
        type = type.value,
        timestamp = timestamp.toEpochMilliseconds(),
        pinned = pinned,
        content = content,
        authorId = author.id.value,
        editedTimestamp = editedTimestamp?.toEpochMilliseconds(),
        referencedMessageId = referencedMessage?.id?.value,
        mentionsEveryone = mentionEveryone,
        hasAttachments = attachments.isNotEmpty(),
        hasEmbeds = embeds.isNotEmpty(),
    )
}
