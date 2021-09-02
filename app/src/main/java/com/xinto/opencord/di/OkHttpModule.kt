package com.xinto.opencord.di

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.util.currentAccountToken
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module

val okHttpModule = module {

    val userAgentHeader = "Discord-Android/${BuildConfig.DISCORD_VERSION_CODE}"

    fun provideAuthOkHttp() = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .addHeader("User-Agent", userAgentHeader)
                .addHeader("Content-Type", "application/json")
                .method(original.method, original.body)
                .build()

            chain.proceed(request)
        }
        .build()

    fun provideOkHttp() = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()

            val request = original.newBuilder()
                .addHeader("User-Agent", userAgentHeader)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", currentAccountToken)
                .method(original.method, original.body)
                .build()

            chain.proceed(request)
        }
        .build()

    single(named("auth")) { provideAuthOkHttp() }
    single(named("normal")) { provideOkHttp() }

}