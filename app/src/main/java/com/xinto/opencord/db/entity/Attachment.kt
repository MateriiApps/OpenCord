package com.xinto.opencord.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EntityMessage::class,
            parentColumns = ["message_id"],
            childColumns = ["message_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class EntityAttachment(
    @PrimaryKey
    @ColumnInfo(name = "attachment_id")
    val id: Long,

    @ColumnInfo(name = "message_id", index = true)
    var messageId: Long = 0,

    @ColumnInfo(name = "filename")
    val filename: String,

    @ColumnInfo(name = "size")
    val size: Int,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "proxy_url")
    val proxyUrl: String,

    @ColumnInfo(name = "width")
    val width: Int? = null,

    @ColumnInfo(name = "height")
    val height: Int? = null,

    @ColumnInfo(name = "content_type")
    val contentType: String = ""
)
