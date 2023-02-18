package com.xinto.opencord.di

import com.xinto.opencord.provider.AnonymousTelemetryProvider
import com.xinto.opencord.provider.PropertyProvider
import com.xinto.opencord.provider.PropertyProviderImpl
import com.xinto.opencord.provider.TelemetryProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val providerModule = module {
    singleOf(::AnonymousTelemetryProvider) bind TelemetryProvider::class
    singleOf(::PropertyProviderImpl) bind PropertyProvider::class
}
