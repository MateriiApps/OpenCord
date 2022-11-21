package com.xinto.opencord.di

import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.DiscordGatewayImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val gatewayModule = module {
    singleOf(::DiscordGatewayImpl) bind DiscordGateway::class
}