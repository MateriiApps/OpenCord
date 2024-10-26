package com.xinto.opencord.ui.viewmodel

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.manager.ActivityManager
import com.xinto.opencord.manager.ClipboardManager
import com.xinto.opencord.manager.DownloadManager
import com.xinto.opencord.manager.ToastManager
import com.xinto.opencord.ui.navigation.ImageViewerData
import kotlinx.coroutines.launch

@Stable
class ImageViewerViewModel(
    private val data: ImageViewerData,
    private val clipboardManager: ClipboardManager,
    private val toastManager: ToastManager,
    private val activityManager: ActivityManager,
    private val downloadManager: DownloadManager,
) : ViewModel() {
    fun copyUrlToClipboard() {
        clipboardManager.setText(data.url)
        toastManager.showToast("Copied to clipboard!")
    }

    fun downloadImage() {
        val uri = try {
            Uri.parse(data.url)
        } catch (_: Throwable) {
            toastManager.showToast("Invalid image url!")
            return
        }

        viewModelScope.launch {
            try {
                downloadManager.downloadToDownloads(
                    uri = uri,
                    outFileName = data.fileName,
                )
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    fun openInBrowser() {
        val uri = try {
            Uri.parse(data.url)
        } catch (_: Throwable) {
            toastManager.showToast("Invalid image url!")
            return
        }

        activityManager.openUrl(uri)
    }
}
