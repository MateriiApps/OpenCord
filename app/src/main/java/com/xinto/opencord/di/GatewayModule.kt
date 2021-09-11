package com.xinto.opencord.di

import com.google.gson.Gson
import com.xinto.opencord.network.gateway.Gateway
import org.koin.dsl.module

val gatewayModule = module {

    fun provideGateway(
        gson: Gson,
    ) = Gateway(gson)

    single { provideGateway(get()) }
}