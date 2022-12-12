package com.xinto.opencord.ui.screens.home.navigation

import com.xinto.taxi.Destination
import kotlinx.parcelize.Parcelize

sealed interface ChannelsNavigation : Destination {
    @Parcelize
    data class Channels(val guildId: Long) : ChannelsNavigation

    @Parcelize
    object None : ChannelsNavigation
}

sealed interface ChatNavigation : Destination {
    @Parcelize
    data class Chat(val channelId: Long) : ChatNavigation

    @Parcelize
    object None : ChatNavigation
}
