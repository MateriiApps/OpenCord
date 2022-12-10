package com.xinto.opencord.db.entity.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xinto.opencord.rest.dto.ApiUser

@Entity(
    tableName = "users",
)
data class EntityUser(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "discriminator")
    val discriminator: String,

    @ColumnInfo(name = "avatar_hash")
    val avatarHash: String?,

    @ColumnInfo(name = "bot")
    val bot: Boolean,

    @ColumnInfo(name = "pronouns")
    val pronouns: String?,

    @ColumnInfo(name = "bio")
    val bio: String?,

    @ColumnInfo(name = "banner_url")
    val bannerUrl: String?,

    @ColumnInfo(name = "public_flags")
    val publicFlags: Int,
)

fun ApiUser.toEntity(): EntityUser {
    return EntityUser(
        id = id.value,
        username = username,
        discriminator = discriminator,
        avatarHash = avatar,
        bot = bot,
        pronouns = pronouns,
        bio = bio,
        bannerUrl = banner,
        publicFlags = publicFlags ?: 0,
    )
}
