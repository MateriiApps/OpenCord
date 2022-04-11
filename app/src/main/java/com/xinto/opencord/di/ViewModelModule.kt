package com.xinto.opencord.di

import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.domain.manager.ActivityManager
import com.xinto.opencord.domain.manager.PersistentDataManager
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.domain.repository.DiscordAuthRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.ui.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
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
        repository: DiscordAuthRepository,
        activityManager: ActivityManager,
        accountManager: AccountManager,
    ): LoginViewModel {
        return LoginViewModel(
            repository = repository,
            activityManager = activityManager,
            accountManager = accountManager
        )
    }


    fun provideChatViewModel(
        gateway: DiscordGateway,
        repository: DiscordApiRepository,
        persistentDataManager: PersistentDataManager
    ): ChatViewModel {
        return ChatViewModel(
            gateway = gateway,
            repository = repository,
            persistentDataManager = persistentDataManager
        )
    }

    fun provideGuildsViewModel(
        gateway: DiscordGateway,
        repository: DiscordApiRepository,
        persistentDataManager: PersistentDataManager
    ): GuildsViewModel {
        return GuildsViewModel(
            gateway = gateway,
            repository = repository,
            persistentDataManager = persistentDataManager
        )
    }

    fun provideChannelsViewModel(
        gateway: DiscordGateway,
        repository: DiscordApiRepository,
        persistentDataManager: PersistentDataManager
    ): ChannelsViewModel {
        return ChannelsViewModel(
            gateway = gateway,
            repository = repository,
            persistentDataManager = persistentDataManager
        )
    }

    fun provideMembersViewModel(
        gateway: DiscordGateway,
        repository: DiscordApiRepository
    ): MembersViewModel {
        return MembersViewModel(
            gateway = gateway,
            repository = repository
        )
    }

    viewModel { provideMainViewModel(get()) }
    viewModel { provideLoginViewModel(get(), get(), get()) }
    viewModel { provideChatViewModel(get(), get(), get()) }
    viewModel { provideGuildsViewModel(get(), get(), get()) }
    viewModel { provideChannelsViewModel(get(), get(), get()) }
    viewModel { provideMembersViewModel(get(), get()) }
}