package com.xinto.opencord.di

import com.xinto.opencord.domain.provider.AnonymousTelemetryProvider
import com.xinto.opencord.domain.provider.PropertyProvider
import com.xinto.opencord.domain.provider.PropertyProviderImpl
import com.xinto.opencord.domain.provider.TelemetryProvider
import org.koin.dsl.module

val providerModule = module {
    fun provideAnonymousTelemetryProvider(): TelemetryProvider {
        return AnonymousTelemetryProvider()
    }

    fun providePropertyProvider(
        telemetryProvider: TelemetryProvider
    ): PropertyProvider {
        return PropertyProviderImpl(
            telemetryProvider = telemetryProvider,
        )
    }

    single { provideAnonymousTelemetryProvider() }
    single { providePropertyProvider(get()) }
}
