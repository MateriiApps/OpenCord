package com.xinto.opencord.domain.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.xinto.opencord.ui.MainActivity

interface ActivityManager {

    fun startMainActivity()

}

class ActivityManagerImpl(
    private val context: Context
) : ActivityManager {

    override fun startMainActivity() {
        startActivity<MainActivity>()
    }

    private inline fun <reified T : Activity> startActivity() {
        context.startActivity(
            Intent(context, T::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        )
    }
}