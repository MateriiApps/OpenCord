package com.xinto.opencord.db.entity.channel

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Immutable
@Entity(
    tableName = "last_message_ids",
)
data class EntityLastMessageId(
    @PrimaryKey
    @ColumnInfo(name = "channel_id")
    val channelId: Long,

    @ColumnInfo(name = "last_message_id")
    val lastMessageId: Long,
)
