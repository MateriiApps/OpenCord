package com.xinto.opencord.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityUser(
    @PrimaryKey
    @ColumnInfo(name = "user_id", index = true)
    val id: Long,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "discriminator")
    val discriminator: String,

    @ColumnInfo(name = "avatar")
    val avatar: String?,

    @ColumnInfo(name = "bot")
    val bot: Boolean = false,
)