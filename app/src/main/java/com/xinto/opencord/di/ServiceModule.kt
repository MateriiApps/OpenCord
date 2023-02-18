package com.xinto.opencord.di

import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.rest.service.DiscordApiServiceImpl
import com.xinto.opencord.rest.service.DiscordAuthService
import com.xinto.opencord.rest.service.DiscordAuthServiceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val serviceModule = module {
    single<DiscordAuthService> {
        DiscordAuthServiceImpl(
            client = get(named("auth")),
        )
    }
    single<DiscordApiService> {
        DiscordApiServiceImpl(
            client = get(named("api")),
        )
    }
}
