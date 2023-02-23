package com.xinto.opencord.manager

import android.content.SharedPreferences
import com.xinto.opencord.db.database.AccountDatabase
import com.xinto.opencord.manager.base.BasePreferenceManager

interface AccountManager {
    var currentAccountToken: String?
    val isLoggedIn: Boolean

    fun getApiCookies(): String?
}

class AccountManagerImpl(
    authPrefs: SharedPreferences,
    private val accountDatabase: AccountDatabase,
) : BasePreferenceManager(authPrefs), AccountManager {
    override var currentAccountToken: String?
        get() = getString(USER_TOKEN_KEY, null)
        set(value) {
            putString(USER_TOKEN_KEY, value)
        }

    override fun getApiCookies(): String? {
        return currentAccountToken?.let {
            accountDatabase.accounts().getCookies(it)
        }
    }

    override val isLoggedIn: Boolean
        get() = currentAccountToken != null

    companion object {
        const val USER_TOKEN_KEY = "user_token"
    }
}
