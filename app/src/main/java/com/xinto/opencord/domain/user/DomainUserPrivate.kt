package com.xinto.opencord.domain.user

import androidx.compose.runtime.Immutable

@Immutable
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
