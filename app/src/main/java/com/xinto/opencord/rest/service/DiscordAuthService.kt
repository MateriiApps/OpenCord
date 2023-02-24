package com.xinto.opencord.rest.service

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.rest.body.LoginBody
import com.xinto.opencord.rest.body.TwoFactorBody
import com.xinto.opencord.rest.models.login.ApiLogin
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DiscordAuthService {
    suspend fun login(body: LoginBody): Pair<ApiLogin, List<Cookie>>
    suspend fun verifyTwoFactor(body: TwoFactorBody, cookies: List<Cookie>?): ApiLogin
}

class DiscordAuthServiceImpl(
    private val client: HttpClient
) : DiscordAuthService {
    override suspend fun login(body: LoginBody): Pair<ApiLogin, List<Cookie>> {
        val url = getLoginUrl()
        return withContext(Dispatchers.IO) {
            val response = client.post(url) {
                setBody(body)
            }

            response.body<ApiLogin>() to getSetCookies(response)
        }
    }

    override suspend fun verifyTwoFactor(body: TwoFactorBody, cookies: List<Cookie>?): ApiLogin {
        val url = getTwoFactorUrl()
        return withContext(Dispatchers.IO) {
            client.post(url) {
                setBody(body)

                if (cookies != null) {
                    header(HttpHeaders.Cookie, cookies.joinToString("; ") { renderCookieHeader(it) })
                }
            }.body()
        }
    }

    private fun getSetCookies(response: HttpResponse): List<Cookie> {
        return response.headers.getAll(HttpHeaders.SetCookie)
            ?.map { parseServerSetCookieHeader(it) } ?: emptyList()
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
