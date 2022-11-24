package com.xinto.opencord.db.entity.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.xinto.opencord.rest.dto.ApiEmbedField

@Entity(
    tableName = "embeds",
    indices = [
        Index(value = ["message_id"]),
    ],
    primaryKeys = [
        "embed_index",
        "message_id",
    ],
)
data class EntityEmbed(
    @ColumnInfo(name = "embed_index")
    val embedIndex: Int,

    @ColumnInfo(name = "message_id")
    val messageId: Long,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "color")
    val color: Int?,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long?,

    @ColumnInfo(name = "author_name")
    val authorName: String?,

    @ColumnInfo(name = "fields")
    val fields: List<ApiEmbedField>?,
)
