package com.xinto.opencord.domain.model

data class DomainUser(
    val id: ULong,
    val username: String,
    val discriminator: String,
    val avatarUrl: String,
    val bot: Boolean = false,
) {
    val formattedDiscriminator = "#$discriminator"
    val tag = "$username$formattedDiscriminator"
}