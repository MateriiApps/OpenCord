package com.xinto.opencord.di

import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.DiscordGatewayImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val gatewayModule = module {
    single<DiscordGateway> {
        DiscordGatewayImpl(
            client = get(named("gatewayHttp")),
            json = get(),
            accountManager = get(),
            propertyProvider = get(),
            logger = get(),
        )
    }
}
