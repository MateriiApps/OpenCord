package com.xinto.opencord.di

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.util.Logger
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
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

    fun provideClient(json: Json, logger: Logger): HttpClient {
        return HttpClient(CIO) {
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                header(HttpHeaders.AcceptLanguage, "en-US")
                header(HttpHeaders.XDiscordLocale, "en-US")
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
            install(WebSockets) {
                maxFrameSize = Long.MAX_VALUE
            }
            if (BuildConfig.DEBUG)
                install(Logging) {
                    level = LogLevel.BODY
                    this.logger = object : io.ktor.client.plugins.logging.Logger {
                        override fun log(message: String) {
                            logger.debug("HTTP", message)
                        }
                    }
                }
        }
    }

    single { provideJson() }
    single { provideClient(get(), get()) }
}
