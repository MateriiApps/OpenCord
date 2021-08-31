package com.xinto.opencord

import android.app.Application
import com.xinto.opencord.di.okHttpModule
import com.xinto.opencord.di.repositoryModule
import com.xinto.opencord.di.retrofitModule
import com.xinto.opencord.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class OpenCord : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@OpenCord)
            modules(
                okHttpModule,
                repositoryModule,
                retrofitModule,
                viewModelModule
            )
        }
    }

}