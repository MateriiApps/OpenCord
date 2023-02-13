package com.xinto.opencord.db.entity.channel

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@Entity(
    tableName = "unread_states",
)
data class EntityUnreadState(
    @PrimaryKey
    @ColumnInfo(name = "channel_id")
    val channelId: Long,

    @ColumnInfo(name = "mention_count")
    val mentionCount: Int,

    @ColumnInfo(name = "last_message_id")
    val lastMessageId: Long,
)
