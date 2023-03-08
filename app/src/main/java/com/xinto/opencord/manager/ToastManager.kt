package com.xinto.opencord.manager

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

interface ToastManager {
    fun showToast(
        @StringRes stringId: Int,
        vararg args: Any,
        duration: Int = Toast.LENGTH_LONG
    )

    fun showToast(
        text: CharSequence,
        duration: Int = Toast.LENGTH_LONG,
    )
}

class ToastManagerImpl(
    private val appContext: Context,
) : ToastManager {
    override fun showToast(stringId: Int, vararg args: Any, duration: Int) {
        Toast.makeText(
            appContext,
            appContext.getString(stringId, *args),
            duration,
        ).show()
    }

    override fun showToast(text: CharSequence, duration: Int) {
        Toast.makeText(
            appContext,
            text,
            duration,
        ).show()
    }
}
