package com.xinto.opencord.ui.navigation

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface AppDestination : Parcelable {
    @Parcelize
    object Main : AppDestination

    @Parcelize
    object Settings : AppDestination

    @Parcelize
    data class Pins(val data: PinsScreenData) : AppDestination
}

@Parcelize
@Immutable
data class PinsScreenData(
    val channelId: Long,
) : Parcelable
