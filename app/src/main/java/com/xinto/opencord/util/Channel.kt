package com.xinto.opencord.util

import com.xinto.opencord.domain.channel.DomainCategoryChannel
import com.xinto.opencord.domain.channel.DomainChannel

/**
 * @return Category with a list of channels inside it.
 * Sort order: Rules, Announcements, Text Channels, Stages, Voice channels
 */
fun getSortedChannels(channels: Collection<DomainChannel>): Map<DomainCategoryChannel?, List<DomainChannel>> {
    val categories = channels.filterIsInstance<DomainCategoryChannel>().sortedBy { it.position }
    val nonCategories = channels.filter { it !is DomainCategoryChannel }
        .sortedWith(compareBy({ it }, { it.position }))

    val sortedChannels = mutableMapOf<DomainCategoryChannel?, List<DomainChannel>>(
        null to nonCategories.filter {
            it.parentId == null
        },
    )

    categories.forEach { category ->
        sortedChannels[category] = nonCategories.filter {
            category.id == it.parentId
        }
    }

    return sortedChannels
}
