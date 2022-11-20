package com.xinto.opencord.di

import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.domain.store.MessageStore
import com.xinto.opencord.domain.store.MessageStoreImpl
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.rest.service.DiscordApiService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val storeModule = module {
    fun provideMessageStore(
        gateway: DiscordGateway,
        api: DiscordApiService,
        cache: CacheDatabase,
    ): MessageStore {
        return MessageStoreImpl(
            gateway = gateway,
            api = api,
            cache = cache,
        )
    }

    singleOf(::provideMessageStore)
}