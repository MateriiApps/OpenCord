package com.xinto.opencord.domain.model

data class DomainUser(
    val id: ULong,
    val username: String,
    val discriminator: String,
    val avatarUrl: String,
    val bot: Boolean = false,
) : Mentionable {
    val tag: String
        get() = "$username$formattedDiscriminator"

    val formattedDiscriminator: String
        get() = "#$discriminator"

    override val formattedMention: String
        get() = "<@$id>"
}
