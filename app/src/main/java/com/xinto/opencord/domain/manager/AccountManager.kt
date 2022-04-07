package com.xinto.opencord.domain.manager

import android.content.SharedPreferences
import androidx.core.content.edit

interface AccountManager {

    var currentAccountToken: String?

    val isLoggedIn: Boolean

}

class AccountManagerImpl(
    private val authPrefs: SharedPreferences
) : AccountManager {

    override var currentAccountToken: String?
        get() = authPrefs.getString(USER_TOKEN_KEY, null)
        set(value) {
            authPrefs.edit {
                putString(USER_TOKEN_KEY, value)
            }
        }

    override val isLoggedIn: Boolean
        get() = currentAccountToken != null

    companion object {
        const val USER_TOKEN_KEY = "user_token"
    }

}