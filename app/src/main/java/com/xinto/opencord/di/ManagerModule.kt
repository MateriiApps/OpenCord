package com.xinto.opencord.di

import android.content.Context
import com.xinto.opencord.domain.manager.*
import com.xinto.opencord.gateway.DiscordGateway
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val managerModule = module {
    fun provideAccountManager(
        context: Context
    ): AccountManager {
        return AccountManagerImpl(
            authPrefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        )
    }

    fun provideActivityManager(
        context: Context
    ): ActivityManager {
        return ActivityManagerImpl(
            context = context
        )
    }

    fun providePersistentDataManager(
        context: Context
    ): PersistentDataManager {
        return PersistentDataManagerImpl(
            persistentPrefs = context.getSharedPreferences("persistent_data", Context.MODE_PRIVATE)
        )
    }

    single { provideAccountManager(androidContext()) }
    single { provideActivityManager(androidContext()) }
    single { providePersistentDataManager(androidContext()) }
}
