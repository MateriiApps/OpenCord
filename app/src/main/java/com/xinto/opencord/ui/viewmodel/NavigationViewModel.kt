package com.xinto.opencord.ui.viewmodel

import android.app.Activity
import androidx.compose.runtime.Stable
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.xinto.opencord.domain.attachment.DomainPictureAttachment
import com.xinto.opencord.ui.navigation.AppDestination
import com.xinto.opencord.ui.navigation.ImageViewerData
import com.xinto.opencord.ui.navigation.PinsScreenData
import com.xinto.opencord.ui.util.topDestination
import dev.olshevski.navigation.reimagined.*

@Stable
@OptIn(SavedStateHandleSaveableApi::class)
class NavigationViewModel constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val nav by savedStateHandle.saveable<NavController<AppDestination>> {
        navController(AppDestination.Main)
    }

    val backstack get() = nav.backstack

    fun openImageViewer(url: String) {
        val data = ImageViewerData(url = url)
        nav.navigate(AppDestination.ImageViewer(data))
    }

    fun openImageViewer(attachment: DomainPictureAttachment) {
        val data = ImageViewerData(
            url = attachment.proxyUrl,
            fileName = attachment.filename,
        )
        nav.navigate(AppDestination.ImageViewer(data))
    }

    fun openPins(channelId: Long) {
        val data = PinsScreenData(channelId = channelId)
        nav.navigate(AppDestination.Pins(data))
    }

    fun resetToMain() = nav.replaceAll(AppDestination.Main)
    fun openSettings() = nav.navigate(AppDestination.Settings)
    fun openMentions() = nav.navigate(AppDestination.Mentions)

    context(Activity)
    fun back() {
        val top = nav.topDestination()

        if (top == AppDestination.Main) {
            finish()
        } else if (backstack.entries.size > 1) {
            nav.pop()
        } else {
            nav.replaceAll(AppDestination.Main)
        }
    }
}
