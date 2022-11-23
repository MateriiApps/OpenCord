package com.xinto.opencord.di

import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.domain.manager.ActivityManager
import com.xinto.opencord.domain.manager.CacheManager
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.rest.service.DiscordAuthService
import com.xinto.opencord.store.ChannelStore
import com.xinto.opencord.store.GuildStore
import com.xinto.opencord.store.MessageStore
import com.xinto.opencord.ui.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    fun provideMainViewModel(
        gateway: DiscordGateway
    ): MainViewModel {
        return MainViewModel(
            gateway = gateway
        )
    }

    fun provideLoginViewModel(
        api: DiscordAuthService,
        activityManager: ActivityManager,
        accountManager: AccountManager
    ): LoginViewModel {
        return LoginViewModel(
            api = api,
            activityManager = activityManager,
            accountManager = accountManager
        )
    }

    fun provideChatViewModel(
        messageStore: MessageStore,
        channelStore: ChannelStore,
        api: DiscordApiService,
        persistentDataManager: PersistentDataManager
    ): ChatViewModel {
        return ChatViewModel(
            messageStore = messageStore,
            channelStore = channelStore,
            api = api,
            persistentDataManager = persistentDataManager
        )
    }

    fun provideGuildsViewModel(
        gateway: DiscordGateway,
        persistentDataManager: PersistentDataManager
    ): GuildsViewModel {
        return GuildsViewModel(
            gateway = gateway,
            persistentDataManager = persistentDataManager
        )
    }

    fun provideChannelsViewModel(
        persistentDataManager: PersistentDataManager,
        channelStore: ChannelStore,
        guildStore: GuildStore,
    ): ChannelsViewModel {
        return ChannelsViewModel(
            persistentDataManager = persistentDataManager,
            channelStore = channelStore,
            guildStore = guildStore,
        )
    }

    fun provideMembersViewModel(
        persistentDataManager: PersistentDataManager,
        gateway: DiscordGateway,
    ): MembersViewModel {
        return MembersViewModel(
            persistentDataManager = persistentDataManager,
            gateway = gateway,
        )
    }

    fun provideCurrentUserViewModel(
        gateway: DiscordGateway,
        api: DiscordApiService,
        cache: CacheManager,
    ): CurrentUserViewModel {
        return CurrentUserViewModel(
            gateway = gateway,
            api = api,
            cache = cache,
        )
    }

    fun provideChannelPinsViewModel(
        persistentDataManager: PersistentDataManager,
        api: DiscordApiService
    ): ChannelPinsViewModel {
        return ChannelPinsViewModel(
            persistentDataManager = persistentDataManager,
            api = api
        )
    }

    viewModelOf(::provideMainViewModel)
    viewModelOf(::provideLoginViewModel)
    viewModelOf(::provideChatViewModel)
    viewModelOf(::provideGuildsViewModel)
    viewModelOf(::provideChannelsViewModel)
    viewModelOf(::provideMembersViewModel)
    viewModelOf(::provideCurrentUserViewModel)
    viewModelOf(::provideChannelPinsViewModel)
}