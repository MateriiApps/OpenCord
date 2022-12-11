package com.xinto.opencord.db.entity.guild

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xinto.opencord.rest.models.ApiGuild

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

    @ColumnInfo(name = "banner")
    val banner: String? = null,

    @ColumnInfo(name = "premium_tier")
    val premiumTier: Int,

    @ColumnInfo(name = "premium_subscription_count")
    val premiumSubscriptionCount: Int?
)

fun ApiGuild.toEntity(): EntityGuild {
    return EntityGuild(
        id = id.value,
        name = name,
        icon = icon,
        banner = banner,
        premiumTier = premiumTier,
        premiumSubscriptionCount = premiumSubscriptionCount,
    )
}
