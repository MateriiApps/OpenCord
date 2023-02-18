package com.xinto.opencord.di

import com.xinto.opencord.util.Logger
import com.xinto.opencord.util.LoggerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val loggerModule = module {
    singleOf(::LoggerImpl) bind Logger::class
}
