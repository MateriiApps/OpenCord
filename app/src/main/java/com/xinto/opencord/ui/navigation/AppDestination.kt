package com.xinto.opencord.ui.navigation

import android.app.Activity
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.xinto.opencord.R
import com.xinto.opencord.ui.util.topDestination
import dev.olshevski.navigation.reimagined.NavController
import dev.olshevski.navigation.reimagined.pop
import dev.olshevski.navigation.reimagined.replaceAll
import kotlinx.parcelize.Parcelize

@Parcelize
sealed interface AppDestination : Parcelable {
    @Parcelize
    object Main : AppDestination

    @Suppress("PROPERTY_WONT_BE_SERIALIZED")
    @Parcelize
    enum class Settings(
        @StringRes val label: Int,
        @DrawableRes val icon: Int,
    ) : AppDestination {
        ACCOUNT(R.string.account, R.drawable.ic_account_circle),
        USER_PROFILE(R.string.user_profile, R.drawable.ic_edit),
        PRIVACY_SAFETY(R.string.privacy_safety, R.drawable.ic_security),
        DEVICES(R.string.devices, R.drawable.ic_devices),
        CONNECTIONS(R.string.connections, R.drawable.ic_link),
        FRIEND_REQUESTS(R.string.friend_requests, R.drawable.ic_person_add),
        VOICE_VIDEO(R.string.voice_video, R.drawable.ic_settings_voice),
        APPEARANCE(R.string.appearance, R.drawable.ic_palette),
        ACCESSIBILITY(R.string.accessibility, R.drawable.ic_settings_accessibility),
        LANGUAGE(R.string.language, R.drawable.ic_language),
        WEB_BROWSER(R.string.web_browser, R.drawable.ic_web),
        NOTIFICATIONS(R.string.notifications, R.drawable.ic_notifications);

        @Parcelize
        companion object : AppDestination
    }

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
