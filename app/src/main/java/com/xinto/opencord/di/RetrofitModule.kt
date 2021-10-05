package com.xinto.opencord.di

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.network.restapi.DiscordAPI
import com.xinto.opencord.network.restapi.DiscordAuthAPI
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {

    val apiUrl = BuildConfig.URL_API + "/"

    fun provideAuthClient(
        okHttpClient: OkHttpClient,
    ) = Retrofit.Builder()
        .baseUrl(apiUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DiscordAuthAPI::class.java)

    fun provideClient(
        okHttpClient: OkHttpClient,
    ) = Retrofit.Builder()
        .baseUrl(apiUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DiscordAPI::class.java)

    single { provideAuthClient(get(qualifier = named("auth"))) }
    single { provideClient(get(qualifier = named("normal"))) }

}