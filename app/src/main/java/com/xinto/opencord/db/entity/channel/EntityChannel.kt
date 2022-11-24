package com.xinto.opencord.db.entity.channel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "channels",
    indices = [
        Index("guild_id"),
        Index("parent_id"),
    ],
)
data class EntityChannel(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "guild_id")
    val guildId: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "type")
    val type: Int,

    @ColumnInfo(name = "position")
    val position: Int,

    @ColumnInfo(name = "parent_id")
    val parentId: Long?,

    @ColumnInfo(name = "nsfw")
    val nsfw: Boolean,
)
