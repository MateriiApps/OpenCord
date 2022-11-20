package com.xinto.opencord.di

import android.content.Context
import androidx.room.Room
import com.xinto.opencord.db.database.CacheDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    fun provideCacheDatabase(context: Context): CacheDatabase {
        val dbFile = context.cacheDir.resolve("cache.db")

        // TODO: remove this
        dbFile.delete()

        return Room.databaseBuilder(
            context,
            CacheDatabase::class.java,
            dbFile.absolutePath,
        ).build()
    }

    singleOf(::provideCacheDatabase)
}