package com.xinto.opencord.di

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.rest.body.XSuperProperties
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.*

private const val userAgent = "Discord-Android/${BuildConfig.DISCORD_VERSION_CODE}"
private val superProperties = XSuperProperties(
    browser = "Discord Android",
    userAgent = userAgent,
    clientBuildNumber = BuildConfig.DISCORD_VERSION_CODE,
    clientBuildVersion = BuildConfig.DISCORD_VERSION_NAME,
    deviceName = "Pixel, oriole",
    os = "Android",
    osSdkVersion = "32",
    osVersion = "12",
    systemLocale = "en-US",
    cpuPerformance = (5..40).random(),
    memoryPerformance = (100_000..800_000).random(),
    cpuCores = 4,
    accessibilitySupport = false,
    accessibilityFeatures = 128,
    deviceAdId = UUID.randomUUID().toString()
)

val HttpHeaders.XSuperProperties: String
    get() = "X-Super-Properties"
val HttpHeaders.XDiscordLocale: String
    get() = "X-Discord-Locale"

val httpModule = module {
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    fun provideAuthClient(
        json: Json
    ): HttpClient {
        return HttpClient(CIO) {
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                header(HttpHeaders.UserAgent, userAgent)
                header(HttpHeaders.AcceptLanguage, "en-US")
                header(HttpHeaders.XDiscordLocale, "en-US")
                header(
                    HttpHeaders.XSuperProperties,
                    json.encodeToString(superProperties).encodeBase64()
                )
            }
            install(HttpRequestRetry) {
                maxRetries = 5
                retryIf { _, httpResponse ->
                    !httpResponse.status.isSuccess()
                }
                retryOnExceptionIf { _, error ->
                    error is HttpRequestTimeoutException
                }
                delayMillis { retry ->
                    retry * 1000L
                }
            }
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    fun provideApiClient(
        json: Json,
        accountManager: AccountManager
    ): HttpClient {
        return HttpClient(CIO) {
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                header(HttpHeaders.Authorization, accountManager.currentAccountToken)
                header(HttpHeaders.UserAgent, userAgent)
                header(HttpHeaders.AcceptLanguage, "en-US")
                header(HttpHeaders.XDiscordLocale, "en-US")
                header(
                    HttpHeaders.XSuperProperties,
                    json.encodeToString(superProperties).encodeBase64()
                )
            }
            install(HttpRequestRetry) {
                maxRetries = 5
                retryIf { _, httpResponse ->
                    !httpResponse.status.isSuccess()
                }
                retryOnExceptionIf { _, error ->
                    error is HttpRequestTimeoutException
                }
                delayMillis { retry ->
                    retry * 2000L
                }
            }
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    fun provideGatewayClient(): HttpClient {
        return HttpClient(CIO) {
            install(WebSockets) {
                maxFrameSize = Long.MAX_VALUE
            }
        }
    }

    single { provideJson() }
    single(named("auth")) { provideAuthClient(get()) }
    single(named("api")) { provideApiClient(get(), get()) }
    single(named("gateway")) { provideGatewayClient() }
}
