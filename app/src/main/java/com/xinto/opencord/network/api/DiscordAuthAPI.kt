package com.xinto.opencord.network.api

import com.xinto.opencord.network.body.LoginBody
import com.xinto.opencord.network.response.ApiLoginResult
import retrofit2.http.Body
import retrofit2.http.POST

interface DiscordAuthAPI {

    @POST("auth/login")
//    @Headers(
//        "User-Agent: Discord-Android/${BuildConfig.DISCORD_VERSION_CODE}",
//        "Content-Type: application/json"
//    )
    suspend fun login(@Body loginBody: LoginBody): ApiLoginResult

}