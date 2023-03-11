package com.xinto.opencord.manager

import android.content.ClipData
import android.content.Context
import android.net.Uri
import androidx.core.content.getSystemService

interface ClipboardManager {
    fun setText(text: String)
    fun setLink(url: String)
}

class ClipboardManagerImpl(
    appContext: Context,
) : ClipboardManager {
    private val clipboard = appContext.getSystemService<android.content.ClipboardManager>()
        ?: error("could not get ClipboardManager")

    override fun setText(text: String) {
        clipboard.setPrimaryClip(ClipData.newPlainText(null, text))
    }

    override fun setLink(url: String) {
        clipboard.setPrimaryClip(ClipData.newRawUri(null, Uri.parse(url)))
    }
}
