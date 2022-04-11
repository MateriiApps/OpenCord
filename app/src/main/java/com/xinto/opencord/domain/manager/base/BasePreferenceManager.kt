package com.xinto.opencord.domain.manager.base

import android.content.SharedPreferences
import androidx.core.content.edit

abstract class BasePreferenceManager(
    private val prefs: SharedPreferences
) {
    
    protected fun getString(key: String, defaultValue: String?): String? {
        return prefs.getString(key, defaultValue)
    }
    
    protected fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }
    
    protected fun getLong(key: String, defaultValue: Long): Long {
        return prefs.getLong(key, defaultValue)
    }
    
    protected fun getInt(key: String, defaultValue: Int): Int {
        return prefs.getInt(key, defaultValue)
    }
    
    protected fun putString(key: String, value: String?) {
        return prefs.edit { putString(key, value) }
    }

    protected fun putBoolean(key: String, value: Boolean) {
        return prefs.edit { putBoolean(key, value) }
    }

    protected fun putLong(key: String, value: Long) {
        return prefs.edit { putLong(key, value) }
    }

    protected fun putInt(key: String, value: Int) {
        return prefs.edit { putInt(key, value) }
    }
    
}