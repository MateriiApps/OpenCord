package com.xinto.opencord.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.db.database.AccountDatabase
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.util.Logger
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.util.concurrent.Executors

val databaseModule = module {
    fun <T : RoomDatabase> RoomDatabase.Builder<T>.installLogging(logger: Logger, logTag: String): RoomDatabase.Builder<T> {
        if (!BuildConfig.DEBUG) return this

        setQueryCallback(
            object : RoomDatabase.QueryCallback {
                override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                    if (sqlQuery.contains("TRANSACTION"))
                        return

                    if (bindArgs.isEmpty()) {
                        logger.debug(logTag, "SQL: $sqlQuery")
                    } else if (logTag == "AccountDatabase") {
                        logger.debug(logTag, "SQL: $sqlQuery\nSQL args: [OpenCord censored]")
                    } else {
                        logger.debug(logTag, "SQL: $sqlQuery\nSQL args: $bindArgs")
                    }
                }
            },
            Executors.newSingleThreadExecutor(),
        )

        return this
    }

    fun provideCacheDatabase(context: Context, logger: Logger): CacheDatabase {
        val dbPath = context.cacheDir.resolve("cache.db").absolutePath

        val db = Room
            .databaseBuilder(context, CacheDatabase::class.java, dbPath)
            .fallbackToDestructiveMigration()
            .installLogging(logger, "CacheDatabase")

        return db.build()
    }

    fun provideAccountDatabase(context: Context, logger: Logger): AccountDatabase {
        val dbPath = context.filesDir.resolve("accounts.db").absolutePath

        val db = Room
            .databaseBuilder(context, AccountDatabase::class.java, dbPath)
            .installLogging(logger, "AccountDatabase")

        return db.build()
    }

    singleOf(::provideCacheDatabase)
    singleOf(::provideAccountDatabase)
}
