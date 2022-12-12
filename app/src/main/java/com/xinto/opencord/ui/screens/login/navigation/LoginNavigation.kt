package com.xinto.opencord.ui.screens.login.navigation

import com.xinto.taxi.Destination
import kotlinx.parcelize.Parcelize

sealed interface LoginScreen : Destination {
    @Parcelize
    object Landing : LoginScreen

    @Parcelize
    object Login : LoginScreen
}
