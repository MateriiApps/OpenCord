package com.xinto.opencord.db.entity.message

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import com.xinto.opencord.rest.models.embed.*

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

    @Embedded(prefix = "author_")
    val author: ApiEmbedAuthor?,

    @Embedded(prefix = "footer_")
    val footer: ApiEmbedFooter?,

    @Embedded(prefix = "thumbnail_")
    val thumbnail: ApiEmbedMedia?,

    @Embedded(prefix = "image_")
    val image: ApiEmbedMedia?,

    @Embedded(prefix = "video_")
    val video: ApiEmbedMedia?,

    @ColumnInfo(name = "fields")
    val fields: List<ApiEmbedField>?,
)

fun ApiEmbed.toEntity(messageId: Long, embedIndex: Int): EntityEmbed {
    return EntityEmbed(
        embedIndex = embedIndex,
        messageId = messageId,
        title = title,
        description = description,
        url = url,
        color = color?.internalColor,
        timestamp = timestamp?.toEpochMilliseconds(),
        footer = footer,
        thumbnail = thumbnail,
        image = image,
        video = video,
        author = author,
        fields = fields,
    )
}
