package com.xinto.opencord.domain.mapper

import com.xinto.opencord.domain.model.DomainLogin
import com.xinto.opencord.rest.dto.ApiLogin

fun ApiLogin.toDomain(): DomainLogin {
    return if (captchaSiteKey != null) {
        DomainLogin.Captcha(
            captchaSiteKey = captchaSiteKey
        )
    } else {
        DomainLogin.Login(
            token = token!!,
            mfa = mfa,
            theme = userSettings!!.theme,
            locale = userSettings.locale
        )
    }
}