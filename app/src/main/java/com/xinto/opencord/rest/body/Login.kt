package com.xinto.opencord.rest.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginBody(
    @SerialName("login")
    val login: String,

    @SerialName("password")
    val password: String,

    @SerialName("captcha_key")
    val captchaKey: String? = null,

    @SerialName("undelete")
    val undelete: Boolean = false,

    @SerialName("login_source")
    val loginSource: String? = null,
)
