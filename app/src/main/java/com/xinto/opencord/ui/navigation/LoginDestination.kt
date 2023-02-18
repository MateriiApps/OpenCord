package com.xinto.opencord.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface LoginDestination : Parcelable {
    @Parcelize
    object Login : LoginDestination

    @Parcelize
    object Landing : LoginDestination
}
