package com.xinto.opencord.manager

import android.content.SharedPreferences
import com.xinto.opencord.db.database.AccountDatabase
import com.xinto.opencord.manager.base.BasePreferenceManager
import com.xinto.opencord.util.DiscordLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface AccountManager {
    var currentAccountToken: String?
    var locale: String?
    val isLoggedIn: Boolean
}

// TODO: refactor this completely
class AccountManagerImpl(
    authPrefs: SharedPreferences,
    private val accounts: AccountDatabase,
) : BasePreferenceManager(authPrefs), AccountManager {
    private val scope = CoroutineScope(Dispatchers.IO)

    override var currentAccountToken: String?
        get() = getString(USER_TOKEN_KEY, null)
        set(value) {
            putString(USER_TOKEN_KEY, value)
        }

    private var _locale = currentAccountToken?.let { token ->
        accounts.accounts().getAccount(token)?.locale
            ?.let { locale -> DiscordLocale.checkDiscordLocale(locale) }
    }
    override var locale: String?
        get() = _locale
        set(value) {
            _locale = value
            scope.launch {
                currentAccountToken?.let { token ->
                    accounts.accounts().setLocale(token, value)
                }
            }
        }

    override val isLoggedIn: Boolean
        get() = currentAccountToken != null

    companion object {
        const val USER_TOKEN_KEY = "user_token"
    }
}
