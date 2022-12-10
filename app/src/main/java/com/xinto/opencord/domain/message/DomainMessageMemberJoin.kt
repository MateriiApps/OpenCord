package com.xinto.opencord.domain.message

import androidx.compose.runtime.Immutable
import com.xinto.opencord.domain.user.DomainUser
import kotlinx.datetime.Instant

@Immutable
data class DomainMessageMemberJoin(
    override val id: Long,
    override val channelId: Long,
    override val timestamp: Instant,
    override val pinned: Boolean,
    override val content: String,
    override val author: DomainUser
) : DomainMessage()
