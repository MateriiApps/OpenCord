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
    private lateinit var mfaTicket: String
    private var secondStageCookies: List<Cookie>? = null

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

    private suspend fun finishLogin(token: String, cookies: List<Cookie>?) {
        val stringCookies = cookies?.joinToString(",") {
            renderSetCookieHeader(it).encodeBase64()
        }

        withContext(Dispatchers.IO) {
            val account = EntityAccount(
                token = token,
                cookies = stringCookies,
            )

            accountDatabase.accounts().insertAccount(account)
        }

        accountManager.currentAccountToken = token
        activityManager.startActivity(AppActivity::class)
    }

    fun login(captchaToken: String? = null) {
        viewModelScope.launch {
            showCaptcha = false
            secondStageCookies = null

            if (username.isEmpty()) {
                usernameError = true
                return@launch
            }

            if (password.isEmpty()) {
                passwordError = true
                return@launch
            }

            try {
                val (apiResponse, cookies) = api.login(
                    LoginBody(
                        login = username,
                        password = password,
                        captchaKey = captchaToken,
                    ),
                )

                when (val response = apiResponse.toDomain()) {
                    is DomainLogin.Login -> {
                        finishLogin(response.token, cookies)
                        secondStageCookies = null
                    }
                    is DomainLogin.TwoFactorAuth -> {
                        mfaTicket = response.ticket
                        secondStageCookies = cookies
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

    fun verifyTwoFactor(code: String) {
        viewModelScope.launch {
            if (code.isEmpty()) {
                mfaError = true
                return@launch
            }

            try {
                showMfa = false

                val response = api.verifyTwoFactor(
                    TwoFactorBody(
                        code = code,
                        ticket = mfaTicket,
                    ),
                    secondStageCookies,
                ).toDomain()

                when (response) {
                    is DomainLogin.Login -> {
                        finishLogin(response.token, secondStageCookies)
                        secondStageCookies = null
                    }
                    is DomainLogin.Captcha -> {
                        showCaptcha = true
                    }
                    is DomainLogin.Error -> {
                        mfaError = true
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateUsername(newUsername: String) {
        username = newUsername
        usernameError = false
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        passwordError = false
    }

    fun updateMfaCode(newMFACode: String) {
        mfaCode = newMFACode
    }

    fun dismissMfa() {
        mfaCode = ""
        showMfa = false
    }
}
