package com.xinto.opencord.domain.model

sealed interface DomainLogin {

    data class Login(
        val token: String,
        val mfa: Boolean,
        val locale: String,
        val theme: String
    ) : DomainLogin

    data class Captcha(val captchaSiteKey: String) : DomainLogin

    data class TwoFactorAuth(val ticket: String) : DomainLogin

    object Error : DomainLogin

}