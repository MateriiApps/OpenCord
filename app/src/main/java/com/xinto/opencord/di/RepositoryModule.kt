package com.xinto.opencord.di

import com.xinto.opencord.network.repository.DiscordAPIRepository
import com.xinto.opencord.network.repository.DiscordAuthAPIRepository
import com.xinto.opencord.network.restapi.DiscordAPI
import com.xinto.opencord.network.restapi.DiscordAuthAPI
import org.koin.dsl.module

val repositoryModule = module {

    fun getAuthRepository(
        api: DiscordAuthAPI
    ) = DiscordAuthAPIRepository(api)

    fun getRepository(
        api: DiscordAPI
    ) = DiscordAPIRepository(api)

    single { getAuthRepository(get()) }
    single { getRepository(get()) }

}