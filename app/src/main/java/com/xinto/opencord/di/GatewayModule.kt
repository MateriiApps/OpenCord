package com.xinto.opencord.di

import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.DiscordGatewayImpl
import com.xinto.opencord.util.Logger
import io.ktor.client.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val gatewayModule = module {

    fun provideGateway(
        client: HttpClient,
        json: Json,
        accountManager: AccountManager,
        logger: Logger
    ): DiscordGateway {
        return DiscordGatewayImpl(
            client = client,
            json = json,
            accountManager = accountManager,
            logger = logger
        )
    }

    single { provideGateway(get(named("gateway")), get(), get(), get()) }
}