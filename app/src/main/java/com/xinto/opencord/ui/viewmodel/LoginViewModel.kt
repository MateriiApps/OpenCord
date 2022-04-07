package com.xinto.opencord.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.domain.manager.ActivityManager
import com.xinto.opencord.domain.model.DomainLogin
import com.xinto.opencord.domain.repository.DiscordAuthRepository
import com.xinto.opencord.rest.body.LoginBody
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: DiscordAuthRepository,
    private val activityManager: ActivityManager,
    private val accountManager: AccountManager,
) : ViewModel() {

    var captchaSiteKey: String? = null
        private set

    var isLoading by mutableStateOf(false)
        private set
    var showCaptcha by mutableStateOf(false)
        private set
    var usernameError by mutableStateOf(false)
        private set
    var passwordError by mutableStateOf(false)
        private set
    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
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
                val response = repository.login(
                    LoginBody(
                        login = username,
                        password = password,
                        captchaKey = captchaToken
                    )
                )
                when (response) {
                    is DomainLogin.Login -> {
                        activityManager.startMainActivity()
                        accountManager.currentAccountToken = response.token
                    }
                    is DomainLogin.Captcha -> {
                        captchaSiteKey = response.captchaSiteKey
                        showCaptcha = true
                    }
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
}