package com.xinto.opencord.manager

import com.xinto.opencord.db.database.AccountDatabase
import com.xinto.opencord.util.throttle
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.date.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.min

interface AccountCookieManager : CookiesStorage

class AccountCookieManagerImpl(
    private val accountManager: AccountManager,
    private val accountDatabase: AccountDatabase,
) : AccountCookieManager {
    private val cookies: MutableList<Cookie> = mutableListOf()
    private var oldestCookie = 0L
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val mutex = Mutex()

    init {
        coroutineScope.launch {
            loadCookies()
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        return mutex.withLock {
            val date = GMTDate()
            if (date.timestamp >= oldestCookie) {
                cleanupCookies(date.timestamp)
            }

            @Suppress("INVISIBLE_MEMBER")
            cookies.filter { it.matches(requestUrl) }
        }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        mutex.withLock {
            if (cookie.name.isBlank()) return@withLock

            @Suppress("INVISIBLE_MEMBER")
            cookies.removeAll { it.name == cookie.name && it.matches(requestUrl) }

            @Suppress("INVISIBLE_MEMBER")
            cookies.add(cookie.fillDefaults(requestUrl))

            cookie.expires?.timestamp?.let { expires ->
                if (oldestCookie > expires) {
                    oldestCookie = expires
                }
            }
        }

        throttledSaveCookies()
    }

    override fun close() {
        coroutineScope.launch {
            mutex.withLock {
                cleanupCookies(GMTDate().timestamp)
            }

            saveCookies()
        }
    }

    private fun cleanupCookies(timestamp: Long) {
        cookies.removeAll { cookie ->
            val expires = cookie.expires?.timestamp ?: return@removeAll false
            expires < timestamp
        }

        val newOldest = cookies.fold(Long.MAX_VALUE) { acc, cookie ->
            cookie.expires?.timestamp?.let { min(acc, it) } ?: acc
        }

        oldestCookie = newOldest
    }

    private suspend fun loadCookies() {
        val stringCookies = accountDatabase.accounts().getCookies(accountManager.currentAccountToken!!)
            ?: return

        val cookies = stringCookies.split(",")
            .map { parseServerSetCookieHeader(it.decodeBase64String()) }

        mutex.withLock {
            this.cookies.clear()
            this.cookies.addAll(cookies)
        }
    }

    private val throttledSaveCookies = throttle(1000L, coroutineScope, ::saveCookies)

    private fun saveCookies() {
        coroutineScope.launch {
            val stringCookies = mutex.withLock {
                cookies.joinToString(",") {
                    renderSetCookieHeader(it).encodeBase64()
                }
            }

            accountDatabase.accounts().setCookies(accountManager.currentAccountToken!!, stringCookies)
        }
    }
}
