package com.xinto.opencord.util

import com.xinto.opencord.domain.model.DomainChannel

/**
 * @return Category with a list of channels inside it.
 * Sort order: Rules, Announcements, Text Channels, Stages, Voice channels
 */
fun getSortedChannels(channels: Collection<DomainChannel>): Map<DomainChannel.Category?, List<DomainChannel>> {
    val categories = channels.filterIsInstance<DomainChannel.Category>().sortedBy { it.position }
    val nonCategories = channels.filter { it !is DomainChannel.Category }
        .sortedWith(compareBy({ it }, { it.position }))

    val sortedChannels = mutableMapOf<DomainChannel.Category?, List<DomainChannel>>(
        null to nonCategories.filter {
            it.parentId == null
        }
    )

    categories.forEach { category ->
        sortedChannels[category] = nonCategories.filter {
            category.id == it.parentId
        }
    }

    return sortedChannels
}