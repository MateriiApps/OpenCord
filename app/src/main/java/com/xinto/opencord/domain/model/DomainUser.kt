package com.xinto.opencord.domain.model

data class DomainUser(
    val id: ULong,
    val username: String,
    val discriminator: String,
    val avatarUrl: String,
    val bot: Boolean = false,
) {

    val formattedDiscriminator
        get() = "#$discriminator"

    val tag
        get() = "$username$formattedDiscriminator"
}