package com.xinto.opencord.network.restapi

import com.xinto.opencord.network.body.LoginBody
import com.xinto.opencord.network.response.ApiLoginResult
import retrofit2.http.Body
import retrofit2.http.POST

interface DiscordAuthAPI {

    @POST("auth/login")
    suspend fun login(@Body loginBody: LoginBody): ApiLoginResult

}