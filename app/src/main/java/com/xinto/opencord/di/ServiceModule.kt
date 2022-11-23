package com.xinto.opencord.di

import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.domain.provider.TelemetryProvider
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.rest.service.DiscordApiServiceImpl
import com.xinto.opencord.rest.service.DiscordAuthService
import com.xinto.opencord.rest.service.DiscordAuthServiceImpl
import io.ktor.client.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val serviceModule = module {
    fun provideDiscordAuthService(
        client: HttpClient
    ): DiscordAuthService {
        return DiscordAuthServiceImpl(
            client = client
        )
    }

    fun provideDiscordApiService(
        client: HttpClient,
        accountManager: AccountManager,
        telemetryProvider: TelemetryProvider,
    ): DiscordApiService {
        return DiscordApiServiceImpl(
            client = client,
            accountManager = accountManager,
            telemetryProvider = telemetryProvider,
        )
    }

    single { provideDiscordAuthService(get()) }
    single { provideDiscordApiService(get(), get(), get()) }
}
