package com.xinto.opencord.domain.model

sealed class DomainUser : Mentionable {
    abstract val id: ULong
    abstract val username: String
    abstract val discriminator: String
    abstract val avatarUrl: String
    abstract val bot: Boolean
    abstract val bio: String?

    override val formattedMention: String
        get() = "<@$id>"

    val formattedDiscriminator: String
        get() = "#$discriminator"

    val tag: String
        get() = "$username$formattedDiscriminator"
}

data class DomainUserPublic(
    override val id: ULong,
    override val username: String,
    override val discriminator: String,
    override val avatarUrl: String,
    override val bot: Boolean,
    override val bio: String?,
    val pronouns: String?,
    val flags: Int, // TODO: implement bitfield
) : DomainUser()

data class DomainUserPrivate(
    override val id: ULong,
    override val username: String,
    override val discriminator: String,
    override val avatarUrl: String,
    override val bot: Boolean,
    override val bio: String?,
    val pronouns: String?,
    val flags: Int,
    val mfaEnabled: Boolean,
    val verified: Boolean,
    val email: String,
    val phone: String?, // TODO: verify this is nullable
    val locale: String,
) : DomainUser()

data class DomainUserReadyEvent(
    override val id: ULong,
    override val username: String,
    override val discriminator: String,
    override val avatarUrl: String,
    override val bot: Boolean,
    override val bio: String?,
    val mfaEnabled: Boolean,
    val verified: Boolean,
    val premium: Boolean,
    val purchasedFlags: Int, // TODO: implement bitfield
) : DomainUser()
