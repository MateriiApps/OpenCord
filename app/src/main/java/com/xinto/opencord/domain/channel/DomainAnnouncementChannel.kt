package com.xinto.opencord.domain.channel

import androidx.compose.runtime.Immutable

@Immutable
data class DomainAnnouncementChannel(
    override val id: Long,
    override val guildId: Long?,
    override val name: String,
    override val position: Int,
    override val parentId: Long?,
    val nsfw: Boolean,
) : DomainChannel()
