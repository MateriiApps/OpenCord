package com.xinto.opencord.di

import android.content.Context
import androidx.room.Room
import com.xinto.opencord.db.OCDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val module = module {
    fun provideDatabase(context: Context): OCDatabase {
        return Room.databaseBuilder(context, OCDatabase::class.java, "database").build()
    }

    singleOf(::provideDatabase)
}