package com.xinto.opencord.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityUser(
    @PrimaryKey
    @ColumnInfo(name = "id", index = true)
    val id: Long,

    @ColumnInfo(name = "username")
    val username: String,

    @ColumnInfo(name = "discriminator")
    val discriminator: String,

    @ColumnInfo(name = "avatar")
    val avatar: String?,

    @ColumnInfo(name = "bot")
    val bot: Boolean,

    @ColumnInfo(name = "pronouns")
    val pronouns: String?,

    @ColumnInfo(name = "bio")
    val bio: String?,
    
    @ColumnInfo(name = "banner")
    val banner: String?,
    
    @ColumnInfo(name = "accent_color")
    val accentColor: String?,

    @ColumnInfo(name = "public_flags")
    val publicFlags: Int?,
    
    @ColumnInfo(name = "flags")
    val privateFlags: Int?,

    @ColumnInfo(name = "verified")
    val verified: Boolean?,

    @ColumnInfo(name = "email")
    val email: String?,

    @ColumnInfo(name = "phone")
    val phone: String?,

    @ColumnInfo(name = "mfa_enabled")
    val mfaEnabled: Boolean?,

    @ColumnInfo(name = "locale")
    val locale: String?,
    
    @ColumnInfo(name = "purchased_flags")
    val purchasedFlags: Int?,

    @ColumnInfo(name = "premium")
    val premium: Boolean?,
)