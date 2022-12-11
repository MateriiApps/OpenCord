package com.xinto.opencord.db.entity.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.xinto.opencord.rest.models.ApiAttachment

@Entity(
    tableName = "attachments",
    indices = [
        Index(value = ["message_id"]),
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

fun ApiAttachment.toEntity(messageId: Long): EntityAttachment {
    return EntityAttachment(
        id = id.value,
        messageId = messageId,
        fileName = filename,
        size = size,
        url = url,
        proxyUrl = proxyUrl,
        width = width,
        height = height,
        contentType = contentType,
    )
}
