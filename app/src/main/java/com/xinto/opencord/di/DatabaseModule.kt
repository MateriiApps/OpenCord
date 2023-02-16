package com.xinto.opencord.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.util.Logger
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.util.concurrent.Executors

val databaseModule = module {
    fun provideCacheDatabase(context: Context, logger: Logger): CacheDatabase {
        val dbPath = context.cacheDir.resolve("cache.db").absolutePath

        val db = Room
            .databaseBuilder(context, CacheDatabase::class.java, dbPath)
            .fallbackToDestructiveMigration()

        if (BuildConfig.DEBUG) {
            db.setQueryCallback(
                object : RoomDatabase.QueryCallback {
                    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                        if (sqlQuery.contains("TRANSACTION"))
                            return

                        if (bindArgs.isEmpty()) {
                            logger.debug("CacheDatabase", "SQL: $sqlQuery")
                        } else {
                            logger.debug("CacheDatabase", "SQL: $sqlQuery\nSQL args: $bindArgs")
                        }
                    }
                },
                Executors.newSingleThreadExecutor(),
            )
        }

        return db.build()
    }

    singleOf(::provideCacheDatabase)
}
