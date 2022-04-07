package com.xinto.opencord.di

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.manager.AccountManager
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val userAgent = "Discord-Android/${BuildConfig.DISCORD_VERSION_CODE}"

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