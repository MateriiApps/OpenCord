package com.xinto.opencord.ui.navigation

import com.xinto.taxi.Destination
import kotlinx.parcelize.Parcelize

sealed interface MainNavigation : Destination {

    @Parcelize
    object Home : MainNavigation

    @Parcelize
    object Settings : MainNavigation

    @Parcelize
    data class Pins(val channelId: Long) : MainNavigation
}
