package com.xinto.opencord.di

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
        client: HttpClient
    ): DiscordApiService {
        return DiscordApiServiceImpl(
            client = client
        )
    }

    single { provideDiscordAuthService(get(named("auth"))) }
    single { provideDiscordApiService(get(named("api"))) }
}