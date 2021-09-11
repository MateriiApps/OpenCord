package com.xinto.opencord.di

import com.xinto.opencord.network.restapi.DiscordAPI
import com.xinto.opencord.network.restapi.DiscordAuthAPI
import com.xinto.opencord.network.util.discordApiUrl
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {

    fun provideAuthClient(
        okHttpClient: OkHttpClient,
    ) = Retrofit.Builder()
        .baseUrl(discordApiUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DiscordAuthAPI::class.java)

    fun provideClient(
        okHttpClient: OkHttpClient,
    ) = Retrofit.Builder()
        .baseUrl(discordApiUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DiscordAPI::class.java)

    single { provideAuthClient(get(qualifier = named("auth"))) }
    single { provideClient(get(qualifier = named("normal"))) }

}