package com.xinto.opencord.domain.activity.types

import androidx.compose.runtime.Immutable
import com.xinto.opencord.domain.activity.ActivityType
import com.xinto.opencord.domain.activity.DomainActivity
import com.xinto.opencord.domain.activity.DomainActivityEmoji

@Immutable
data class DomainActivityCustom(
    override val name: String,
    override val createdAt: Long,
    val status: String?,
    val emoji: DomainActivityEmoji?,
) : DomainActivity {
    override val type = ActivityType.Custom
}
