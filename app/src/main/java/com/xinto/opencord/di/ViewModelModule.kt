package com.xinto.opencord.di

import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.domain.manager.ActivityManager
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
        repository: DiscordApiRepository
    ): ChatViewModel {
        return ChatViewModel(
            gateway = gateway,
            repository = repository
        )
    }

    fun provideGuildsViewModel(
        gateway: DiscordGateway,
        repository: DiscordApiRepository
    ): GuildsViewModel {
        return GuildsViewModel(
            gateway = gateway,
            repository = repository
        )
    }

    fun provideChannelsViewModel(
        gateway: DiscordGateway,
        repository: DiscordApiRepository
    ): ChannelsViewModel {
        return ChannelsViewModel(
            gateway = gateway,
            repository = repository
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
    viewModel { provideChatViewModel(get(), get()) }
    viewModel { provideGuildsViewModel(get(), get()) }
    viewModel { provideChannelsViewModel(get(), get()) }
    viewModel { provideMembersViewModel(get(), get()) }
}