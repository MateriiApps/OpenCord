package com.xinto.opencord.db.entity.message

import androidx.room.*

@Entity(
    tableName = "attachments",
    indices = [
        Index(value = ["message_id"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = EntityMessage::class,
            parentColumns = ["id"],
            childColumns = ["message_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
)
data class EntityAttachment(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "message_id")
    val messageId: Long,

    @ColumnInfo(name = "file_name")
    val fileName: String,

    @ColumnInfo(name = "size")
    val size: Int,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "proxy_url")
    val proxyUrl: String,

    @ColumnInfo(name = "width")
    val width: Int?,

    @ColumnInfo(name = "height")
    val height: Int?,

    @ColumnInfo(name = "content_type")
    val contentType: String?,
)
