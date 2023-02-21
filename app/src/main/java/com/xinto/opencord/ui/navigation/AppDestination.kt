package com.xinto.opencord.ui.navigation

import android.app.Activity
import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.xinto.opencord.ui.util.topDestination
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.replaceAll
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

context(Activity)
fun NavController<AppDestination>.back() {
    val top = topDestination()

    if (top == AppDestination.Main) {
        finish()
    } else if (backstack.entries.size > 1) {
        pop()
    } else {
        replaceAll(AppDestination.Main)
    }
}
