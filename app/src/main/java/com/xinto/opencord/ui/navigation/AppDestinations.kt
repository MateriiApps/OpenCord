package com.xinto.opencord.ui.navigation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface AppDestinations : Parcelable {
    @Parcelize
    object Main : AppDestinations

    @Parcelize
    object Settings : AppDestinations

    @Parcelize
    data class Pins(val data: PinsScreenData) : AppDestinations
}

@Parcelize
@Immutable
data class PinsScreenData(
    val channelId: Long,
) : Parcelable
