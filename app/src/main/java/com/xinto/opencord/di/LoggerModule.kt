package com.xinto.opencord.di

import com.xinto.opencord.util.Logger
import com.xinto.opencord.util.LoggerImpl
import org.koin.dsl.module

val loggerModule = module {
    fun provideLogger(): Logger {
        return LoggerImpl()
    }

    single { provideLogger() }
}
