package com.xinto.opencord

import android.app.Application
import com.xinto.opencord.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class OpenCord : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@OpenCord)
            modules(
                gatewayModule,
                httpModule,
                managerModule,
                repositoryModule,
                serviceModule,
                simpleAstModule,
                viewModelModule,
                loggerModule,
            )
        }
    }

}