package com.xinto.opencord.di

import android.content.Context
import com.xinto.opencord.domain.manager.*
import com.xinto.opencord.proto.serializer.AccountsSerializer
import com.xinto.opencord.proto.serializer.PersistentDataSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val managerModule = module {

    fun provideAccountManager(
        context: Context,
        accountsSerializer: AccountsSerializer,
    ): AccountManager {
        return AccountManagerImpl(
            context = context,
            accountsSerializer = accountsSerializer
        )
    }

    fun provideActivityManager(context: Context): ActivityManager {
        return ActivityManagerImpl(context)
    }

    fun providePersistentDataManager(
        context: Context,
        persistentDataSerializer: PersistentDataSerializer,
    ): PersistentDataManager {
        return PersistentDataManagerImpl(
            context = context,
            persistentDataSerializer = persistentDataSerializer,
        )
    }

    single { provideAccountManager(androidContext(), get()) }
    single { provideActivityManager(androidContext()) }
    single { providePersistentDataManager(androidContext(), get()) }
}