package com.xinto.opencord.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

abstract class BaseNavigationScreen(val route: String) : Parcelable {
    override fun equals(other: Any?): Boolean {
        return other is BaseNavigationScreen && this.route == other.route
    }

    override fun hashCode(): Int {
        return route.hashCode()
    }
}

sealed class MainScreen(route: String) : BaseNavigationScreen(route) {

    @Parcelize
    object Home : MainScreen("home")

    @Parcelize
    object Settings : MainScreen("settings")

}

sealed class LoginScreen(route: String) : BaseNavigationScreen(route) {

    @Parcelize
    object Landing : LoginScreen("landing")

    @Parcelize
    object Login : LoginScreen("login")

}