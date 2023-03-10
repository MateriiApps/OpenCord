package com.xinto.opencord.di

import android.content.Context
import com.xinto.opencord.manager.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val managerModule = module {
    fun provideAccountManager(
        context: Context,
    ): AccountManager {
        return AccountManagerImpl(
            authPrefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE),
        )
    }

    fun providePersistentDataManager(
        context: Context
    ): PersistentDataManager {
        return PersistentDataManagerImpl(
            persistentPrefs = context.getSharedPreferences("persistent_data", Context.MODE_PRIVATE),
        )
    }

    single { provideAccountManager(androidContext()) }
    single { providePersistentDataManager(androidContext()) }
    single<ActivityManager> { ActivityManagerImpl(androidContext()) }
    singleOf(::AccountCookieManagerImpl) bind AccountCookieManager::class
    singleOf(::ClipboardManagerImpl) bind ClipboardManager::class
    singleOf(::ToastManagerImpl) bind ToastManager::class
}
