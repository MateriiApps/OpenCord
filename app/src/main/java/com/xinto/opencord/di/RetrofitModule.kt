package com.xinto.opencord.di

import com.xinto.opencord.network.api.DiscordAuthAPI
import com.xinto.opencord.network.util.discordApiUrl
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {

    fun provideAuthClient(
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .baseUrl(discordApiUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DiscordAuthAPI::class.java)

    single { provideAuthClient(get()) }

}