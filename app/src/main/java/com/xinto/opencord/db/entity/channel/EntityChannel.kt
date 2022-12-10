package com.xinto.opencord.db.entity.channel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.xinto.opencord.rest.models.ApiChannel

@Entity(
    tableName = "channels",
    indices = [
        Index(value = ["guild_id"]),
    ],
)
data class EntityChannel(
    // -------- Discord data -------- //
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

    // -------- DB relational data -------- //
    @ColumnInfo(name = "is_pins_stored")
    val pinsStored: Boolean,
)

fun ApiChannel.toEntity(guildId: Long): EntityChannel {
    return EntityChannel(
        id = id.value,
        guildId = this.guildId?.value ?: guildId,
        name = name,
        type = type,
        position = position,
        parentId = parentId?.value,
        nsfw = nsfw,
        pinsStored = false,
    )
}
