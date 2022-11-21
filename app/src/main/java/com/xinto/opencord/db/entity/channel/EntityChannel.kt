package com.xinto.opencord.db.entity.channel

import androidx.room.*
import com.xinto.opencord.db.entity.guild.EntityGuild

@Entity(
    tableName = "channels",
    indices = [
        Index("guild_id"),
        Index("parent_id"),
    ],
    foreignKeys = [
        ForeignKey(
            entity = EntityGuild::class,
            parentColumns = ["id"],
            childColumns = ["guild_id"],
            onDelete = ForeignKey.CASCADE,
        ),
//        ForeignKey(
//            entity = EntityChannel::class,
//            parentColumns = ["id"],
//            childColumns = ["parent_id"],
//            onDelete = ForeignKey.SET_NULL,
//        ),
    ]
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
