package com.xinto.opencord.di

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.manager.AccountCookieManager
import com.xinto.opencord.manager.AccountManager
import com.xinto.opencord.provider.PropertyProvider
import com.xinto.opencord.provider.TelemetryProvider
import com.xinto.opencord.util.DiscordLocale
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private typealias OCLogger = com.xinto.opencord.util.Logger
private typealias KtorLogger = Logger

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

    fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installLogging(logger: OCLogger) {
        if (!BuildConfig.DEBUG) return

        install(Logging) {
            level = LogLevel.BODY
            this.logger = object : KtorLogger {
                override fun log(message: String) {
                    logger.debug("HTTP", message)
                }
            }
        }
    }

    fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installRetry() {
        install(HttpRequestRetry) {
            maxRetries = 5
            retryIf { _, res -> res.status.value in (500 until 600) }
            retryOnExceptionIf { _, error ->
                error is HttpRequestTimeoutException
            }
            delayMillis { retryCount ->
                retryCount * 2000L
            }
        }
    }

    fun OkHttpConfig.addStripKtorHeadersInterceptor() {
        addInterceptor { chain ->
            val request = chain.request()
            val requestBuilder = request
                .newBuilder()
                .removeHeader(HttpHeaders.Accept)
                .removeHeader(HttpHeaders.AcceptCharset)

            chain.proceed(requestBuilder.build())
        }
    }

    fun provideAuthClient(
        json: Json,
        logger: OCLogger,
        telemetryProvider: TelemetryProvider,
        propertyProvider: PropertyProvider,
    ): HttpClient {
        val superProperties = json.encodeToString(propertyProvider.xSuperProperties).encodeBase64()
        val locale = DiscordLocale.getSystemDiscordLocale()

        return HttpClient(OkHttp) {
            defaultRequest {
                contentType(ContentType.Application.Json)
                userAgent(telemetryProvider.userAgent)
                header(HttpHeaders.AcceptLanguage, "en-US")
                header(HttpHeaders.XDiscordLocale, locale)
                header(HttpHeaders.XSuperProperties, superProperties)
            }

            install(ContentNegotiation) {
                json(json)
            }

            installRetry()
            installLogging(logger)

            engine {
                addStripKtorHeadersInterceptor()
            }
        }
    }

    fun provideApiClient(
        json: Json,
        logger: OCLogger,
        accountManager: AccountManager,
        accountCookies: AccountCookieManager,
        telemetryProvider: TelemetryProvider,
        propertyProvider: PropertyProvider,
    ): HttpClient {
        return HttpClient(OkHttp) {
            val superProperties = json
                .encodeToString(propertyProvider.xSuperProperties)
                .encodeBase64()

            defaultRequest {
                contentType(ContentType.Application.Json)
                userAgent(telemetryProvider.userAgent)
                header(HttpHeaders.Authorization, accountManager.currentAccountToken)
                header(HttpHeaders.AcceptLanguage, "en-US")
                header(HttpHeaders.XDiscordLocale, accountManager.locale ?: DiscordLocale.getSystemDiscordLocale())
                header(HttpHeaders.XSuperProperties, superProperties)
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(HttpCookies) {
                storage = accountCookies
            }

            installRetry()
            installLogging(logger)

            engine {
                addStripKtorHeadersInterceptor()
            }
        }
    }

    fun provideGatewayClient(
        logger: OCLogger,
        accountCookies: AccountCookieManager,
    ): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                header(HttpHeaders.AcceptEncoding, "gzip")
                header(HttpHeaders.UserAgent, "okhttp/4.8.0")
            }

            install(WebSockets) {
                maxFrameSize = Long.MAX_VALUE

                extensions {
                    install(WebSocketDeflateExtension) {
                        compressIf { false }
                        clientNoContextTakeOver = false
                    }
                }
            }

            install(HttpCookies) {
                storage = accountCookies
            }

            installRetry()
            installLogging(logger)

            engine {
                addStripKtorHeadersInterceptor()
            }
        }
    }

    singleOf(::provideJson)
    singleOf(::provideAuthClient) {
        qualifier = named("authHttp")
    }
    singleOf(::provideApiClient) {
        qualifier = named("apiHttp")
    }
    singleOf(::provideGatewayClient) {
        qualifier = named("gatewayHttp")
    }
}
