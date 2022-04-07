package com.xinto.opencord.di

import android.content.Context
import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.domain.manager.AccountManagerImpl
import com.xinto.opencord.domain.manager.ActivityManager
import com.xinto.opencord.domain.manager.ActivityManagerImpl
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

    single { provideAccountManager(androidContext()) }
    single { provideActivityManager(androidContext()) }
}