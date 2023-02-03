package com.xinto.opencord.rest.models.user

import com.xinto.opencord.domain.user.DomainUser
import com.xinto.opencord.domain.user.DomainUserPublic
import com.xinto.opencord.rest.models.ApiSnowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiUser(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("username")
    val username: String,

    @SerialName("discriminator")
    val discriminator: String,

    @SerialName("avatar")
    val avatar: String?,

    @SerialName("bot")
    val bot: Boolean = false,

    // Not yet public feature
    @SerialName("pronouns")
    val pronouns: String? = null,

    @SerialName("bio")
    val bio: String? = null,

//    @SerialName("banner_color")
//    val banner_color: Any? = null,

    @SerialName("banner")
    val banner: String? = null,

    // Not yet public feature
//    @SerialName("avatar_decoration")
//    val avatarDecoration: String? = null,

    @SerialName("accent_color")
    val accentColor: Int? = null,

    // Not present on the "user" field of the READY event
    @SerialName("public_flags")
    val publicFlags: Int? = null,


    /* Private user object fields */

    @SerialName("flags")
    val privateFlags: Int? = null,

    @SerialName("verified")
    val verified: Boolean? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("phone")
    val phone: String? = null,

    @SerialName("mfa_enabled")
    val mfaEnabled: Boolean? = null,

    @SerialName("locale")
    val locale: String? = null,


    /* Only present on the "user" field of the READY event */

    @SerialName("purchased_flags")
    val purchasedFlags: Int? = null,

    @SerialName("premium")
    val premium: Boolean? = null,
)

// TODO: figure out a way to get rid of this
fun DomainUser.toApi(): ApiUser {
    val avatarUrl = if (avatarUrl.contains("/embed/")) {
        null
    } else {
        avatarUrl.takeLastWhile { it != '/' }.takeWhile { it != '.' }
    }

    return when (this) {
        is DomainUserPublic -> ApiUser(
            id = ApiSnowflake(id),
            username = username,
            discriminator = discriminator,
            avatar = avatarUrl,
            bot = bot,
            pronouns = pronouns,
            bio = bio,
            banner = null,
            accentColor = null,
            publicFlags = flags,
            privateFlags = null,
            verified = null,
            email = null,
            phone = null,
            mfaEnabled = null,
            locale = null,
            purchasedFlags = null,
            premium = null,
        )
        else -> throw UnsupportedOperationException("Cannot convert other DomainUser types to ApiUser")
    }
}
