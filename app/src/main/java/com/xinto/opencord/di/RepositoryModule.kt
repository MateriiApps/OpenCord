package com.xinto.opencord.di

import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.domain.repository.DiscordApiRepositoryImpl
import com.xinto.opencord.domain.repository.DiscordAuthRepository
import com.xinto.opencord.domain.repository.DiscordAuthRepositoryImpl
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.rest.service.DiscordAuthService
import org.koin.dsl.module

val repositoryModule = module {

    fun provideDiscordAuthRepository(
        service: DiscordAuthService
    ): DiscordAuthRepository {
        return DiscordAuthRepositoryImpl(
            service = service
        )
    }

    fun provideDiscordApiRepository(
        gateway: DiscordGateway,
        service: DiscordApiService
    ): DiscordApiRepository {
        return DiscordApiRepositoryImpl(
            gateway = gateway,
            service = service
        )
    }

    single { provideDiscordAuthRepository(get()) }
    single { provideDiscordApiRepository(get(), get()) }
}