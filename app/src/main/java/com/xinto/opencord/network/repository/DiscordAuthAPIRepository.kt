package com.xinto.opencord.network.repository

import com.xinto.opencord.domain.model.DomainLoginResult
import com.xinto.opencord.domain.model.DomainLoginUserSettingsResult
import com.xinto.opencord.network.body.LoginBody
import com.xinto.opencord.network.restapi.DiscordAuthAPI
import com.xinto.opencord.network.util.getResultOrError

class DiscordAuthAPIRepository(
    private val api: DiscordAuthAPI
) {

    suspend fun login(loginBody: LoginBody) =
        getResultOrError {
            with(api.login(loginBody)) {
                DomainLoginResult(
                    token = token,
                    mfa = mfa,
                    userSettings = with(user_settings) {
                        DomainLoginUserSettingsResult(
                            locale = locale,
                            theme = theme,
                        )
                    }
                )
            }

        }

}