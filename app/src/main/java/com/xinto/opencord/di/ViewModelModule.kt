package com.xinto.opencord.di

import com.xinto.opencord.manager.AccountManager
import com.xinto.opencord.manager.ActivityManager
import com.xinto.opencord.manager.PersistentDataManager
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.rest.service.DiscordApiService
import com.xinto.opencord.rest.service.DiscordAuthService
import com.xinto.opencord.store.*
import com.xinto.opencord.ui.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    fun provideMainViewModel(
        gateway: DiscordGateway
    ): MainViewModel {
        return MainViewModel(
            gateway = gateway,
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
            accountManager = accountManager,
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
            persistentDataManager = persistentDataManager,
        )
    }

    fun provideGuildsViewModel(
        guildStore: GuildStore,
        persistentDataManager: PersistentDataManager
    ): GuildsViewModel {
        return GuildsViewModel(
            guildStore = guildStore,
            persistentDataManager = persistentDataManager,
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

    fun provideCurrentUserViewModel(
        gateway: DiscordGateway,
        sessionStore: SessionStore,
        currentUserStore: CurrentUserStore,
        userSettingsStore: UserSettingsStore,
    ): CurrentUserViewModel {
        return CurrentUserViewModel(
            gateway = gateway,
            sessionStore = sessionStore,
            currentUserStore = currentUserStore,
            userSettingsStore = userSettingsStore,
        )
    }

    fun provideChannelPinsViewModel(
        persistentDataManager: PersistentDataManager,
        messageStore: MessageStore,
    ): ChannelPinsViewModel {
        return ChannelPinsViewModel(
            persistentDataManager = persistentDataManager,
            messageStore = messageStore,
        )
    }

    viewModelOf(::provideMainViewModel)
    viewModelOf(::provideLoginViewModel)
    viewModelOf(::provideChatViewModel)
    viewModelOf(::provideGuildsViewModel)
    viewModelOf(::provideChannelsViewModel)
    viewModelOf(::provideCurrentUserViewModel)
    viewModelOf(::provideChannelPinsViewModel)
}
