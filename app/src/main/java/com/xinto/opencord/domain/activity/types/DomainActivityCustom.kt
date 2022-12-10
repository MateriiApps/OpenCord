package com.xinto.opencord.domain.activity.types

import com.xinto.opencord.domain.activity.ActivityType
import com.xinto.opencord.domain.activity.DomainActivity
import com.xinto.opencord.domain.activity.DomainActivityEmoji

data class DomainActivityCustom(
    override val name: String,
    override val createdAt: Long,
    val status: String?,
    val emoji: DomainActivityEmoji?,
) : DomainActivity {
    override val type = ActivityType.Custom
}
