package com.xinto.opencord.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlin.reflect.KClass

interface ActivityManager {
    fun startActivity(activity: KClass<out Activity>, clearOld: Boolean = true)
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
}
