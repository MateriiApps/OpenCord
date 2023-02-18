package com.xinto.opencord

import android.app.Application
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.di.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class OpenCord : Application() {
    private val scope = MainScope()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@OpenCord)
            modules(
                gatewayModule,
                httpModule,
                managerModule,
                serviceModule,
                simpleAstModule,
                viewModelModule,
                loggerModule,
                providerModule,
                databaseModule,
                storeModule,
                hcaptchaModule,
            )
        }

        scope.launch(Dispatchers.IO) {
            val db = get<CacheDatabase>()

            db.apply {
                messages().clear()
                embeds().clear()
                attachments().clear()
                users().deleteUnusedUsers()
            }
        }
    }
}
