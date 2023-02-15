package com.xinto.opencord.di

import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.store.*
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

    fun provideChannelStore(
        gateway: DiscordGateway,
        cache: CacheDatabase,
    ): ChannelStore {
        return ChannelStoreImpl(
            gateway = gateway,
            cache = cache,
        )
    }

    fun provideGuildStore(
        gateway: DiscordGateway,
        cache: CacheDatabase,
    ): GuildStore {
        return GuildStoreImpl(
            gateway = gateway,
            cache = cache,
        )
    }

    fun provideUserSettingsStore(
        gateway: DiscordGateway,
        api: DiscordApiService,
    ): UserSettingsStore {
        return UserSettingsStoreImpl(
            gateway = gateway,
            api = api,
        )
    }

    fun provideCurrentUserStore(
        gateway: DiscordGateway,
    ): CurrentUserStore {
        return CurrentUserStoreImpl(
            gateway = gateway,
        )
    }

    fun provideSessionStore(
        gateway: DiscordGateway,
    ): SessionStore {
        return SessionStoreImpl(
            gateway = gateway,
        )
    }

    fun provideUnreadStore(
        gateway: DiscordGateway,
        api: DiscordApiService,
        cacheDatabase: CacheDatabase,
    ): UnreadStore {
        return UnreadStoreImpl(
            gateway = gateway,
            api = api,
            cache = cacheDatabase,
        )
    }

    singleOf(::provideMessageStore)
    singleOf(::provideChannelStore)
    singleOf(::provideGuildStore)
    singleOf(::provideUserSettingsStore)
    singleOf(::provideCurrentUserStore)
    singleOf(::provideSessionStore)
    singleOf(::provideUnreadStore)
}
