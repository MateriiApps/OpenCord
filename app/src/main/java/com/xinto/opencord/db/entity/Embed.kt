package com.xinto.opencord.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

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
data class EntityEmbed(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "message_id", index = true)
    var messageId: Long = 0,

    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "url")
    val url: String? = null,

    @ColumnInfo(name = "color")
    val color: Int? = null,

    @ColumnInfo(name = "timestamp")
    val timestamp: Instant? = null,

//    @Embedded
//    val author: EntityEmbedAuthor? = null,
//
//    @ColumnInfo(name = "fields")
//    val fields: List<EntityEmbedField>? = null
)

@Serializable
data class EntityEmbedAuthor(
    @ColumnInfo(name = "name")
    val name: String
)

@Serializable
data class EntityEmbedField(
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "value")
    val value: String,
)