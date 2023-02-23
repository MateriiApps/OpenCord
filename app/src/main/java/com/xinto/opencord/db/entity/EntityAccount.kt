package com.xinto.opencord.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "accounts",
)
data class EntityAccount(
    @PrimaryKey
    @ColumnInfo(name = "token")
    val token: String,

    @ColumnInfo(name = "user_id")
    val userId: Long? = null,

    @ColumnInfo(name = "username")
    val username: String? = null,

    @ColumnInfo(name = "discriminator")
    val discriminator: String? = null,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null,

    @ColumnInfo(name = "cookies")
    val cookies: String? = null,
)
