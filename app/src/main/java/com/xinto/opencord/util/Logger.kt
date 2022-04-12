package com.xinto.opencord.util

import android.util.Log
import com.xinto.opencord.BuildConfig

interface Logger {

    fun debug(tag: String, message: String)

}

class LoggerImpl : Logger {

    override fun debug(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }
}