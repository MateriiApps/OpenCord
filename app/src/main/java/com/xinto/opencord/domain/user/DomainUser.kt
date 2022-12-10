package com.xinto.opencord.domain.user

import com.xinto.opencord.db.entity.user.EntityUser
import com.xinto.opencord.domain.Mentionable
import com.xinto.opencord.rest.dto.ApiUser
import com.xinto.opencord.rest.service.DiscordCdnServiceImpl

abstract class DomainUser : Mentionable {
    abstract val id: Long
    abstract val username: String
    abstract val discriminator: String
    abstract val avatarUrl: String
    abstract val bot: Boolean
    abstract val bio: String?
    abstract val flags: Int // TODO: implement bitfield

    override val formattedMention: String
        get() = "<@$id>"

    val formattedDiscriminator: String
        get() = "#$discriminator"

    val tag: String
        get() = "$username$formattedDiscriminator"
}

fun ApiUser.toDomain(): DomainUser {
    val avatarUrl = avatar
        ?.let { DiscordCdnServiceImpl.getUserAvatarUrl(id.toString(), it) }
        ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(discriminator.toInt().rem(5))

    return when {
        locale != null -> DomainUserPrivate(
            id = id.value,
            username = username,
            discriminator = discriminator,
            avatarUrl = avatarUrl,
            bot = bot,
            bio = bio,
            flags = (publicFlags ?: 0) or (privateFlags ?: 0),
            pronouns = pronouns,
            mfaEnabled = mfaEnabled!!,
            verified = verified!!,
            email = email!!,
            phone = phone,
            locale = locale,
        )
        premium != null -> DomainUserPrivateReady(
            id = id.value,
            username = username,
            discriminator = discriminator,
            avatarUrl = avatarUrl,
            bot = bot,
            bio = bio,
            flags = privateFlags ?: 0,
            mfaEnabled = mfaEnabled!!,
            verified = verified!!,
            premium = premium,
            purchasedFlags = purchasedFlags!!,
        )
        else -> DomainUserPublic(
            id = id.value,
            username = username,
            discriminator = discriminator,
            avatarUrl = avatarUrl,
            bot = bot,
            bio = bio,
            flags = (publicFlags ?: 0) or (privateFlags ?: 0),
            pronouns = pronouns,
        )
    }
}

fun EntityUser.toDomain(): DomainUser {
    val avatarUrl = avatarHash
        ?.let { DiscordCdnServiceImpl.getUserAvatarUrl(id.toString(), it) }
        ?: DiscordCdnServiceImpl.getDefaultAvatarUrl(discriminator.toInt().rem(5))

    return DomainUserPublic(
        id = id,
        username = username,
        discriminator = discriminator,
        avatarUrl = avatarUrl,
        bot = bot,
        bio = bio,
        flags = publicFlags,
        pronouns = pronouns,
    )
}
