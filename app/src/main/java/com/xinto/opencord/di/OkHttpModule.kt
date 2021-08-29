package com.xinto.opencord.di

import com.xinto.opencord.BuildConfig
import okhttp3.OkHttpClient
import org.koin.dsl.module

val okHttpModule = module {

    fun provideOkHttp() = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .addHeader("User-Agent", "Discord-Android/${BuildConfig.VERSION_CODE}")
                .method(original.method(), original.body())
                .build()

            chain.proceed(request)
        }
        .build()

    single { provideOkHttp() }

}