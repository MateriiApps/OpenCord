package com.xinto.opencord.di

import android.content.Context
import androidx.room.Room
import com.xinto.opencord.db.database.CacheDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    fun provideCacheDatabase(context: Context): CacheDatabase {
        return Room.databaseBuilder(
            context,
            CacheDatabase::class.java,
            context.cacheDir.resolve("cache").absolutePath,
        ).build()
    }

    singleOf(::provideCacheDatabase)
}