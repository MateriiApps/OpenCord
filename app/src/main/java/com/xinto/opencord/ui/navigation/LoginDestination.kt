package com.xinto.opencord.ui.navigation

import android.app.Activity
import android.os.Parcelable
import com.xinto.opencord.ui.util.topDestination
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.replaceAll
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface LoginDestination : Parcelable {
    @Parcelize
    object Login : LoginDestination

    @Parcelize
    object Landing : LoginDestination
}

context(Activity)
fun NavController<LoginDestination>.back() {
    val top = topDestination()

    if (top == LoginDestination.Landing) {
        finish()
    } else if (backstack.entries.size > 1) {
        pop()
    } else {
        replaceAll(LoginDestination.Landing)
    }
}
