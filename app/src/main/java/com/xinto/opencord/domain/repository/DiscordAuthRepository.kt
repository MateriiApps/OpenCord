package com.xinto.opencord.domain.repository

import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainLogin
import com.xinto.opencord.rest.body.LoginBody
import com.xinto.opencord.rest.service.DiscordAuthService

interface DiscordAuthRepository {

    suspend fun login(body: LoginBody): DomainLogin

}

class DiscordAuthRepositoryImpl(
    private val service: DiscordAuthService
) : DiscordAuthRepository {

    override suspend fun login(body: LoginBody): DomainLogin {
        val result = service.login(body)
        return result.toDomain()
    }
}