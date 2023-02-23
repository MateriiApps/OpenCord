package com.xinto.opencord.di

import android.content.Context
import com.xinto.opencord.db.database.AccountDatabase
import com.xinto.opencord.manager.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val managerModule = module {
    fun provideAccountManager(
        context: Context,
        accountDatabase: AccountDatabase,
    ): AccountManager {
        return AccountManagerImpl(
            authPrefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE),
            accountDatabase = accountDatabase,
        )
    }

    fun providePersistentDataManager(
        context: Context
    ): PersistentDataManager {
        return PersistentDataManagerImpl(
            persistentPrefs = context.getSharedPreferences("persistent_data", Context.MODE_PRIVATE),
        )
    }

    single { provideAccountManager(androidContext(), get()) }
    single { providePersistentDataManager(androidContext()) }
    single<ActivityManager> { ActivityManagerImpl(androidContext()) }
}
