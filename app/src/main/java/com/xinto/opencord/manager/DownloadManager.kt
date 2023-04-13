package com.xinto.opencord.manager

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.getSystemService
import com.xinto.opencord.BuildConfig
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import android.app.DownloadManager as AndroidDownloadManager

interface DownloadManager {
    /**
     * Initiates and waits for a downloads with the system's Download Manager.
     * Returns once download is complete
     *
     * @param uri Target download URL
     * @param outFileName Out file name otherwise defaults to uri's last path segment
     * @param cancellable Whether the download will be cancelled if this coroutine scope is
     */
    suspend fun downloadToDownloads(
        uri: Uri,
        outFileName: String? = uri.lastPathSegment,
        cancellable: Boolean = false,
    )
}

class DownloadManagerImpl(
    private val appContext: Context,
) : DownloadManager {
    private val manager = appContext.getSystemService<AndroidDownloadManager>()!!

    override suspend fun downloadToDownloads(uri: Uri, outFileName: String?, cancellable: Boolean) {
        return suspendCancellableCoroutine { continuation ->
            val downloadId = AndroidDownloadManager.Request(uri).run {
                addRequestHeader("User-Agent", "OpenCord/${BuildConfig.VERSION_NAME}")
                setNotificationVisibility(AndroidDownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, outFileName ?: "unknown")

                // Disable gzip on emulator due to https compression bug
                if (Build.PRODUCT == "google_sdk") {
                    addRequestHeader("Accept-Encoding", "")
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    @Suppress("DEPRECATION")
                    allowScanningByMediaScanner()
                }

                manager.enqueue(this) // returns downloadId
            }

            val receiver = Receiver(downloadId, manager, continuation)

            appContext.registerReceiver(
                receiver,
                IntentFilter(AndroidDownloadManager.ACTION_DOWNLOAD_COMPLETE),
            )

            if (cancellable) {
                continuation.invokeOnCancellation {
                    manager.remove(downloadId)
                    appContext.unregisterReceiver(receiver)
                }
            }
        }
    }

    private class Receiver(
        private val downloadId: Long,
        private val manager: AndroidDownloadManager,
        private val continuation: Continuation<Unit>,
    ) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(AndroidDownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id != downloadId) {
                return
            }

            val (status, reason) = AndroidDownloadManager.Query().run {
                setFilterById(downloadId)

                val cursor = manager.query(this)

                @SuppressLint("Range")
                if (!cursor.moveToFirst()) {
                    // Notification "cancel" button was clicked
                    null to null
                } else {
                    val status = cursor.run { getInt(getColumnIndex(AndroidDownloadManager.COLUMN_STATUS)) }
                    val reason = cursor.run { getInt(getColumnIndex(AndroidDownloadManager.COLUMN_REASON)) }
                    status to reason
                }
            }


            when (status) {
                AndroidDownloadManager.STATUS_SUCCESSFUL -> {
                    context.unregisterReceiver(this@Receiver)
                    continuation.resume(Unit)
                }
                AndroidDownloadManager.STATUS_FAILED -> {
                    context.unregisterReceiver(this@Receiver)
                    continuation.resumeWithException(Error("Enqueued download returned with an error: ${reasonToString(reason!!)}"))
                }
                null -> {
                    context.unregisterReceiver(this@Receiver)
                    continuation.resumeWithException(Error("Enqueued download was cancelled"))
                }
                else -> {
                    // Continue receiving intents
                }
            }
        }

        private fun reasonToString(reason: Int) = when (reason) {
            AndroidDownloadManager.ERROR_UNKNOWN -> "Unknown Error"
            AndroidDownloadManager.ERROR_FILE_ERROR -> "File Error"
            AndroidDownloadManager.ERROR_UNHANDLED_HTTP_CODE -> "Unhandled HTTP Code"
            AndroidDownloadManager.ERROR_HTTP_DATA_ERROR -> "HTTP Data Error"
            AndroidDownloadManager.ERROR_TOO_MANY_REDIRECTS -> "Too many redirects"
            AndroidDownloadManager.ERROR_INSUFFICIENT_SPACE -> "Insufficient space"
            AndroidDownloadManager.ERROR_DEVICE_NOT_FOUND -> "Device not found"
            AndroidDownloadManager.ERROR_CANNOT_RESUME -> "Cannot resume download"
            AndroidDownloadManager.ERROR_FILE_ALREADY_EXISTS -> "File already exists"
            else -> "Unknown error"
        }
    }
}
