package com.xinto.opencord.domain.activity.types

import androidx.compose.runtime.Immutable
import com.xinto.opencord.domain.activity.ActivityType
import com.xinto.opencord.domain.activity.DomainActivity
import com.xinto.opencord.domain.emoji.DomainEmoji

@Immutable
data class DomainActivityCustom(
    override val name: String,
    override val createdAt: Long,
    val status: String?,
    val emoji: DomainEmoji?,
) : DomainActivity {
    override val type = ActivityType.Custom
}
