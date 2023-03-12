package com.xinto.opencord.domain.channel

import androidx.compose.runtime.Immutable

@Immutable
data class DomainCategoryChannel(
    override val id: Long,
    override val guildId: Long?,
    override val name: String,
    override val position: Int,
    override val parentId: Long? = null,
) : DomainChannel() {
    override val sortingPriority: Short get() = 0
}
