package com.xinto.opencord.domain.user

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
