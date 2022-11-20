package com.xinto.opencord.db.entity.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.xinto.opencord.rest.dto.ApiMessageType

@Entity(
    tableName = "messages",
    indices = [
        Index(value = ["channel_id"]),
    ],
)
data class EntityMessage(
    // -------- Message data -------- //
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "channel_id")
    val channelId: Long,

    @ColumnInfo(name = "type")
    val type: ApiMessageType,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

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
