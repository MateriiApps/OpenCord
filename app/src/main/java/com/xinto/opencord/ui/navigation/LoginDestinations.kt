package com.xinto.opencord.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface LoginDestinations : Parcelable {
    @Parcelize
    object Login : LoginDestinations

    @Parcelize
    object LoginLanding : LoginDestinations
}
