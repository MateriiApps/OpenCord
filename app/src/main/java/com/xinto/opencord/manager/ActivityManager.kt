package com.xinto.opencord.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import kotlin.reflect.KClass

interface ActivityManager {
    fun startActivity(activity: KClass<out Activity>, clearOld: Boolean = true)
    fun openUrl(uri: Uri)
}

class ActivityManagerImpl(
    private val appContext: Context
) : ActivityManager {
    override fun startActivity(activity: KClass<out Activity>, clearOld: Boolean) {
        val flags = if (clearOld) {
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        } else {
            Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val intent = Intent(appContext, activity.java)
            .apply { this.flags = flags }

        appContext.startActivity(intent)
    }

    override fun openUrl(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
            .apply { data = uri }
            .apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }

        appContext.startActivity(intent)
    }
}
