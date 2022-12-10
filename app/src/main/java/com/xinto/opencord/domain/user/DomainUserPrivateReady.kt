package com.xinto.opencord.domain.user

import androidx.compose.runtime.Immutable

/**
 * Sent on the `user` field of the READY event
 */
@Immutable
data class DomainUserPrivateReady(
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
