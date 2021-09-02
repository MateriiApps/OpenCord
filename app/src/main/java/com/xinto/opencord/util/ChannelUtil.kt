package com.xinto.opencord.util

import com.xinto.opencord.domain.model.DomainChannel

/**
 * @return Category with a list of channels inside it.
 * Sort order: Rules, Announcements, Text Channels, Stages, Voice channels
 */
fun getSortedChannels(
    channels: List<DomainChannel>
): Map<DomainChannel.Category?, List<DomainChannel>> {
    val categories = channels.filterIsInstance<DomainChannel.Category>().sortedBy { it.position }
    val textChannels = channels.filterIsInstance<DomainChannel.TextChannel>().sortedBy { it.position }
    val voiceChannels = channels.filterIsInstance<DomainChannel.VoiceChannel>().sortedBy { it.position }

    val sortedChannels = mutableMapOf<DomainChannel.Category?, List<DomainChannel>>(
        null to textChannels.filter {
            it.parentId == null
        } + voiceChannels.filter {
            it.parentId == null
        }
    )

    categories.forEach { category ->
        sortedChannels[category] = textChannels.filter {
            category.id == it.parentId
        } + voiceChannels.filter {
            category.id == it.parentId
        }
    }

    return sortedChannels
}