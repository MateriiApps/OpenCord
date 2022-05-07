package com.xinto.opencord.domain.repository

import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainLogin
import com.xinto.opencord.rest.body.LoginBody
import com.xinto.opencord.rest.body.TwoFactorBody
import com.xinto.opencord.rest.service.DiscordAuthService

interface DiscordAuthRepository {

    suspend fun login(body: LoginBody): DomainLogin

    suspend fun verifyTwoFactor(body: TwoFactorBody): DomainLogin

}

class DiscordAuthRepositoryImpl(
    private val service: DiscordAuthService
) : DiscordAuthRepository {

    override suspend fun login(body: LoginBody): DomainLogin {
        val result = service.login(body)
        return result.toDomain()
    }

    override suspend fun verifyTwoFactor(body: TwoFactorBody): DomainLogin {
        val result = service.verifyTwoFactor(body)
        return result.toDomain()
    }
}