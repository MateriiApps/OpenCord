package com.xinto.opencord.di

import android.content.Context
import com.xinto.opencord.domain.manager.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val managerModule = module {

    fun provideAccountManager(context: Context): AccountManager {
        return AccountManagerImpl(context)
    }

    fun provideActivityManager(context: Context): ActivityManager {
        return ActivityManagerImpl(context)
    }

    fun providePersistentDataManager(context: Context): PersistentDataManager {
        return PersistentDataManagerImpl(context)
    }

    single { provideAccountManager(androidContext()) }
    single { provideActivityManager(androidContext()) }
    single { providePersistentDataManager(androidContext()) }
}