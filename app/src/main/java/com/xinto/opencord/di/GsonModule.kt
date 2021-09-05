package com.xinto.opencord.di

import com.google.gson.GsonBuilder
import org.koin.dsl.module

val gsonModule = module {

    fun provideGson() = GsonBuilder().create()

    single { provideGson() }

}