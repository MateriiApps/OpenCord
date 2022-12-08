package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.domain.manager.ActivityManager
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainLogin
import com.xinto.opencord.rest.body.LoginBody
import com.xinto.opencord.rest.body.TwoFactorBody
import com.xinto.opencord.rest.service.DiscordAuthService
import kotlinx.coroutines.launch

class LoginViewModel(
    private val api: DiscordAuthService,
    private val activityManager: ActivityManager,
    private val accountManager: AccountManager
) : ViewModel() {
    lateinit var mfaTicket: String
        private set

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

    fun login(captchaToken: String? = null) {
        viewModelScope.launch {
            showCaptcha = false

            if (username.isEmpty()) {
                usernameError = true
                return@launch
            }

            if (password.isEmpty()) {
                passwordError = true
                return@launch
            }

            try {
                val response = api.login(
                    LoginBody(
                        login = username,
                        password = password,
                        captchaKey = captchaToken,
                    ),
                ).toDomain()

                when (response) {
                    is DomainLogin.Login -> {
                        activityManager.startMainActivity()
                        accountManager.currentAccountToken = response.token
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
                ).toDomain()
                when (response) {
                    is DomainLogin.Login -> {
                        activityManager.startMainActivity()
                        accountManager.currentAccountToken = response.token
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
