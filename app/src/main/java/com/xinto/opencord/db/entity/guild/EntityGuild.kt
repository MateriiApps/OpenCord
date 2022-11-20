package com.xinto.opencord.db.entity.guild

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "guilds",
)
data class EntityGuild(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "icon")
    val icon: String?,

    @ColumnInfo(name = "banner_url")
    val bannerUrl: String? = null,

    @ColumnInfo(name = "permissions")
    val permissions: Long,

    @ColumnInfo(name = "premium_tier")
    val premiumTier: Int,

    @ColumnInfo(name = "premium_subscription_count")
    val premiumSubscriptionCount: Int?
)
