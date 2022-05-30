package com.xinto.opencord.di

import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.domain.provider.TelemetryProvider
import com.xinto.opencord.domain.provider.PropertyProvider
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
        json: Json,
        telemetryProvider: TelemetryProvider,
        propertyProvider: PropertyProvider
    ): HttpClient {
        return HttpClient(CIO) {
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                header(HttpHeaders.UserAgent, telemetryProvider.userAgent)
                header(HttpHeaders.AcceptLanguage, "en-US")
                header(HttpHeaders.XDiscordLocale, "en-US")
                header(
                    HttpHeaders.XSuperProperties,
                    json.encodeToString(propertyProvider.xSuperProperties).encodeBase64()
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
        accountManager: AccountManager,
        telemetryProvider: TelemetryProvider,
        propertyProvider: PropertyProvider
    ): HttpClient {
        return HttpClient(CIO) {
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                header(HttpHeaders.Authorization, accountManager.currentAccountToken)
                header(HttpHeaders.UserAgent, telemetryProvider.userAgent)
                header(HttpHeaders.AcceptLanguage, "en-US")
                header(HttpHeaders.XDiscordLocale, "en-US")
                header(
                    HttpHeaders.XSuperProperties,
                    json.encodeToString(propertyProvider.xSuperProperties).encodeBase64()
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
    single(named("auth")) { provideAuthClient(get(), get(), get()) }
    single(named("api")) { provideApiClient(get(), get(), get(), get()) }
    single(named("gateway")) { provideGatewayClient() }
}
