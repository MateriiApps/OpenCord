package com.xinto.opencord.domain.model

sealed class DomainUser : Mentionable {
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

data class DomainUserPublic(
    override val id: Long,
    override val username: String,
    override val discriminator: String,
    override val avatarUrl: String,
    override val bot: Boolean,
    override val bio: String?,
    override val flags: Int,
    val pronouns: String?,
) : DomainUser()

data class DomainUserPrivate(
    override val id: Long,
    override val username: String,
    override val discriminator: String,
    override val avatarUrl: String,
    override val bot: Boolean,
    override val bio: String?,
    override val flags: Int,
    val pronouns: String?,
    val mfaEnabled: Boolean,
    val verified: Boolean,
    val email: String,
    val phone: String?, // TODO: verify this is nullable
    val locale: String,
) : DomainUser()

data class DomainUserReadyEvent(
    override val id: Long,
    override val username: String,
    override val discriminator: String,
    override val avatarUrl: String,
    override val bot: Boolean,
    override val bio: String?,
    override val flags: Int,
    val mfaEnabled: Boolean,
    val verified: Boolean,
    val premium: Boolean,
    val purchasedFlags: Int, // TODO: implement bitfield
) : DomainUser()
