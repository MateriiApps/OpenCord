package com.xinto.opencord.di

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.manager.AccountManager
import com.xinto.opencord.provider.PropertyProvider
import com.xinto.opencord.provider.TelemetryProvider
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

    fun <T : HttpClientEngineConfig> HttpClientConfig<T>.installCookies() {
        install(HttpCookies) {
            // TODO: persist cookies to disk later
            storage = AcceptAllCookiesStorage()
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
        return HttpClient(OkHttp) {
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                header(HttpHeaders.UserAgent, telemetryProvider.userAgent)
                header(HttpHeaders.AcceptLanguage, "en-US")
                header(HttpHeaders.XDiscordLocale, "en-US")
                header(HttpHeaders.XSuperProperties, superProperties)
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

            installCookies()
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
        telemetryProvider: TelemetryProvider,
        propertyProvider: PropertyProvider
    ): HttpClient {
        return HttpClient(OkHttp) {
            val superProperties = json
                .encodeToString(propertyProvider.xSuperProperties)
                .encodeBase64()

            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                header(HttpHeaders.Authorization, accountManager.currentAccountToken)
                header(HttpHeaders.UserAgent, telemetryProvider.userAgent)
                header(HttpHeaders.AcceptLanguage, "en-US")
                header(HttpHeaders.XDiscordLocale, "en-US")
                header(HttpHeaders.XSuperProperties, superProperties)
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

            installCookies()
            installLogging(logger)

            engine {
                addStripKtorHeadersInterceptor()
            }
        }
    }

    fun provideGatewayClient(logger: OCLogger): HttpClient {
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

            install(HttpRequestRetry) {
                maxRetries = 3
                retryOnExceptionIf { _, error ->
                    error is HttpRequestTimeoutException
                }
                delayMillis { retry ->
                    retry * 2000L
                }
            }

            installLogging(logger)

            engine {
                addStripKtorHeadersInterceptor()
            }
        }
    }

    singleOf(::provideJson)
    single(named("auth")) { provideAuthClient(get(), get(), get(), get()) }
    single(named("api")) { provideApiClient(get(), get(), get(), get(), get()) }
    single(named("gateway")) { provideGatewayClient(get()) }
}
