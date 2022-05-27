package com.xinto.opencord.di

import android.content.Context
import androidx.room.Room
import com.xinto.opencord.db.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    fun provideRoomDb(context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    }

    single { provideRoomDb(androidContext()) }
}