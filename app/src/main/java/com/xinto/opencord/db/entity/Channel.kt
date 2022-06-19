package com.xinto.opencord.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channels")
data class EntityChannel(
    @PrimaryKey
    @ColumnInfo(name = "channel_id")
    val id: Long,

    @ColumnInfo(name = "channel_guild_id")
    val guildId: Long?,

    @ColumnInfo(name = "channel_name")
    val name: String,

    @ColumnInfo(name = "channel_type")
    val type: Int,

    @ColumnInfo(name = "channel_position")
    val position: Int,

    @ColumnInfo(name = "channel_parent_id")
    val parentId: Long?,

    @ColumnInfo(name = "channel_nsfw")
    val nsfw: Boolean,

    @ColumnInfo(name = "channel_permissions")
    val permissions: Long,
)