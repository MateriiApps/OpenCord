package com.xinto.opencord.ui.screens.home.panels.channel.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.xinto.opencord.domain.channel.DomainCategoryChannel

@Stable
class CategoryItemData(
    channel: DomainCategoryChannel,
    collapsed: Boolean,
    subChannels: List<ChannelItemData>?,
) {
    var channel by mutableStateOf(channel)
    var collapsed by mutableStateOf(collapsed)
    var channels = mutableStateMapOf<Long, ChannelItemData>()

    val channelsSorted by derivedStateOf {
        channels.values.sortedWith { a, b -> a.channel compareTo b.channel }
    }

    init {
        if (subChannels != null) {
            channels.putAll(subChannels.associateBy { it.channel.id })
        }
    }
}