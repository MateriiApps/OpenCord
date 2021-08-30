package com.xinto.opencord.network.repository

import com.xinto.opencord.network.api.DiscordAuthAPI
import com.xinto.opencord.network.body.LoginBody
import com.xinto.opencord.network.util.getResultOrError

class DiscordAuthAPIRepository(
    private val api: DiscordAuthAPI
) {

    suspend fun login(loginBody: LoginBody) =
        getResultOrError { api.login(loginBody) }

}