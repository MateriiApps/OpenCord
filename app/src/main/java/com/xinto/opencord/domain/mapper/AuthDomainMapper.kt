package com.xinto.opencord.domain.mapper

import com.xinto.opencord.domain.model.DomainLogin
import com.xinto.opencord.rest.dto.ApiLogin

fun ApiLogin.toDomain(): DomainLogin {
    return when {
        ticket != null -> {
            DomainLogin.TwoFactorAuth(
                ticket = ticket
            )
        }
        captchaSiteKey != null -> {
            DomainLogin.Captcha(
                captchaSiteKey = captchaSiteKey
            )
        }
        token != null -> {
            DomainLogin.Login(
                token = token,
                mfa = mfa,
                theme = userSettings?.theme ?: "",
                locale = userSettings?.locale ?: ""
            )
        }
        else -> DomainLogin.Error
    }
}