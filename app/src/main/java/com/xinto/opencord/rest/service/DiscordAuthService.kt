package com.xinto.opencord.rest.service

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.rest.body.LoginBody
import com.xinto.opencord.rest.body.TwoFactorBody
import com.xinto.opencord.rest.models.login.ApiLogin
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DiscordAuthService {
    suspend fun login(body: LoginBody): ApiLogin
    suspend fun verifyTwoFactor(body: TwoFactorBody): ApiLogin
}

class DiscordAuthServiceImpl(
    private val client: HttpClient
) : DiscordAuthService {
    override suspend fun login(body: LoginBody): ApiLogin {
        val url = getLoginUrl()
        return withContext(Dispatchers.IO) {
            client.post(url) {
                setBody(body)
            }.body()
        }
    }

    override suspend fun verifyTwoFactor(body: TwoFactorBody): ApiLogin {
        val url = getTwoFactorUrl()
        return withContext(Dispatchers.IO) {
            client.post(url) {
                setBody(body)
            }.body()
        }
    }

    private companion object {
        const val BASE = BuildConfig.URL_API

        fun getLoginUrl(): String {
            return "$BASE/auth/login"
        }

        fun getTwoFactorUrl(): String {
            return "$BASE/auth/mfa/totp"
        }
    }
}
