package com.xinto.opencord.di

import com.xinto.opencord.proto.serializer.AccountsSerializer
import com.xinto.opencord.proto.serializer.PersistentDataSerializer
import com.xinto.opencord.util.Logger
import org.koin.dsl.module

val protoModule = module {

    fun providePersistentDataSerializer(
        logger: Logger
    ): PersistentDataSerializer {
        return PersistentDataSerializer(
            logger = logger
        )
    }

    fun provideAccountsSerializer(
        logger: Logger
    ): AccountsSerializer {
        return AccountsSerializer(
            logger = logger
        )
    }

    single { providePersistentDataSerializer(get()) }
    single { provideAccountsSerializer(get()) }
}