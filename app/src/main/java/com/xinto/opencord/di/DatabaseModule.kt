package com.xinto.opencord.di

import android.content.Context
import androidx.room.Room
import com.xinto.opencord.BuildConfig
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.util.Logger
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import java.util.concurrent.Executors

val databaseModule = module {
    fun provideCacheDatabase(context: Context, logger: Logger): CacheDatabase {
        val db = Room.databaseBuilder(
            context,
            CacheDatabase::class.java,
            context.cacheDir.resolve("cache.db").absolutePath,
        )

        if (BuildConfig.DEBUG) {
            db.setQueryCallback(
                { sql, args ->
                    logger.debug("CacheDatabase", "SQL: $sql")
                    if (args.isNotEmpty()) {
                        logger.debug("CacheDatabase", "SQL args: $args")
                    }
                },
                Executors.newSingleThreadExecutor()
            )
        }

        return db.build()
    }

    singleOf(::provideCacheDatabase)
}