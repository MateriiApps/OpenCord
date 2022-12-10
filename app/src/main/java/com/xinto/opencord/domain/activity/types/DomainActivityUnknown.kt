package com.xinto.opencord.domain.activity.types

import com.xinto.opencord.domain.activity.ActivityType
import com.xinto.opencord.domain.activity.DomainActivity

// TODO: remove this once all activity types are implemented
data class DomainActivityUnknown(
    override val name: String,
    override val createdAt: Long,
) : DomainActivity {
    override val type = ActivityType.Unknown
}
