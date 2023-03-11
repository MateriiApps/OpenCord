package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.db.database.AccountDatabase
import com.xinto.opencord.db.entity.EntityAccount
import com.xinto.opencord.domain.login.DomainLogin
import com.xinto.opencord.domain.login.toDomain
import com.xinto.opencord.manager.AccountManager
import com.xinto.opencord.manager.ActivityManager
import com.xinto.opencord.rest.body.LoginBody
import com.xinto.opencord.rest.body.TwoFactorBody
import com.xinto.opencord.rest.service.DiscordAuthService
import com.xinto.opencord.ui.AppActivity
import com.xinto.opencord.util.throttle
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val api: DiscordAuthService,
    private val accountManager: AccountManager,
    private val accountDatabase: AccountDatabase,
    private val activityManager: ActivityManager,
) : ViewModel() {
    private var mfaTicket: String? = null
    private var fingerprint: String? = null
    private var cookies: List<Cookie>? = null

    var isLoading by mutableStateOf(false)
        private set

    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var mfaCode by mutableStateOf("")
        private set

    var showCaptcha by mutableStateOf(false)
        private set
    var showMfa by mutableStateOf(false)
        private set

    var usernameError by mutableStateOf(false)
        private set
    var passwordError by mutableStateOf(false)
        private set
    var mfaError by mutableStateOf(false)
        private set

    val login = throttle(1000L, viewModelScope) { captchaToken: String? ->
        showCaptcha = false

        if (username.isEmpty()) {
            usernameError = true
            return@throttle
        }

        if (password.isEmpty()) {
            passwordError = true
            return@throttle
        }

        viewModelScope.launch {
            try {
                val fingerprint = getFingerprint()

                val response = api.login(
                    LoginBody(
                        login = username,
                        password = password,
                        undelete = false,
                        captchaKey = captchaToken,
                    ),
                    existingCookies = cookies,
                    xFingerprint = fingerprint,
                ).toDomain()

                when (response) {
                    is DomainLogin.Login -> {
                        finishLogin(response.token, response.locale)
                    }
                    is DomainLogin.TwoFactorAuth -> {
                        mfaTicket = response.ticket
                        showMfa = true
                    }
                    is DomainLogin.Captcha -> {
                        showCaptcha = true
                    }
                    is DomainLogin.Error -> {
                        usernameError = true
                        passwordError = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val verify2fa = throttle(1000L, viewModelScope) {
        if (mfaCode.isEmpty()) {
            mfaError = true
            return@throttle
        }

        showMfa = false

        if (fingerprint == null || mfaTicket == null) {
            mfaCode = ""
            mfaTicket = null
            return@throttle
        }

        viewModelScope.launch {
            try {
                val response = api.verifyTwoFactor(
                    TwoFactorBody(
                        code = mfaCode,
                        ticket = mfaTicket!!,
                    ),
                    cookies,
                    fingerprint!!,
                ).toDomain()

                when (response) {
                    is DomainLogin.Login -> {
                        finishLogin(response.token, response.locale)
                    }
                    is DomainLogin.TwoFactorAuth -> {
                        error("double mfa")
                    }
                    is DomainLogin.Captcha -> {
                        showCaptcha = true
                    }
                    is DomainLogin.Error -> {
                        mfaError = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun finishLogin(token: String, locale: String) {
        val stringCookies = cookies?.joinToString(",") {
            renderSetCookieHeader(it).encodeBase64()
        }

        withContext(Dispatchers.IO) {
            val account = EntityAccount(
                token = token,
                cookies = stringCookies,
                fingerprint = fingerprint,
                locale = locale,
            )

            accountDatabase.accounts().insertAccount(account)
        }

        username = ""
        password = ""
        showMfa = false
        mfaTicket = null
        fingerprint = null
        cookies = null

        accountManager.currentAccountToken = token
        activityManager.startActivity(AppActivity::class)
    }

    private suspend fun getFingerprint(): String {
        return this.fingerprint ?: api.getFingerprint().let { (fingerprint, cookies) ->
            this.fingerprint = fingerprint
            this.cookies = cookies

            fingerprint
        }
    }

    fun updateUsername(newUsername: String) {
        username = newUsername
        usernameError = false
        cookies = null
        fingerprint = null
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        passwordError = false
        cookies = null
        fingerprint = null
    }

    fun updateMfaCode(newMFACode: String) {
        mfaCode = newMFACode
    }

    fun dismissMfa() {
        showMfa = false
        mfaCode = ""
    }
}
