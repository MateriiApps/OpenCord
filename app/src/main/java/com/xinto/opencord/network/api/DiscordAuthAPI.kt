package com.xinto.opencord.network.api

import com.xinto.opencord.network.body.LoginBody
import retrofit2.http.Body
import retrofit2.http.POST

interface DiscordAuthAPI {

    @POST("auth/login")
    fun login(@Body loginBody: LoginBody): LoginBody

}