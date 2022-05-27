package com.xinto.opencord.db.entity

import androidx.room.*
import com.xinto.enumgetter.GetterGen
import kotlinx.datetime.Instant

data class EntityMessageFull(
    @Embedded
    val message: EntityMessage,

    @Relation(
        parentColumn = "message_id",
        entityColumn = "message_id"
    )
    val attachments: List<EntityAttachment>,

//    @Relation(
//        parentColumn = "id",
//        entityColumn = "message_id"
//    )
//    val embeds: List<EntityEmbed>,
//
//    @Embedded
//    val referencedMessage: EntityMessageFull?,
)

@Entity(tableName = "messages")
data class EntityMessage(
    @PrimaryKey
    @ColumnInfo(name = "message_id", index = true)
    val id: Long,

    @ColumnInfo(name = "channel_id")
    val channelId: Long,

    @ColumnInfo(name = "timestamp")
    val timestamp: Instant,

    @ColumnInfo(name = "edited_timestamp")
    val editedTimestamp: Instant?,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "type")
    val type: EntityMessageType,

    @Embedded
    val author: EntityUser,
)

@GetterGen
enum class EntityMessageType(val value: Int) {
    Default(0),
    GuildMemberJoin(7),
    Reply(19);

    companion object
}