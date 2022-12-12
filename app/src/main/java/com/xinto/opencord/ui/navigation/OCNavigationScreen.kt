package com.xinto.opencord.ui.navigation

import com.xinto.taxi.Destination
import kotlinx.parcelize.Parcelize

sealed interface MainScreen : Destination {
    @Parcelize
    object Home : MainScreen

    @Parcelize
    data class Pins(val channelId: Long) : MainScreen

    @Parcelize
    object Settings : MainScreen
}

