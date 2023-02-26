package com.xinto.opencord.rest.body

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginBody(
    @SerialName("login")
    val login: String,

    @SerialName("password")
    val password: String,

    @SerialName("undelete")
    val undelete: Boolean,

    @SerialName("captcha_key")
    val captchaKey: String? = null,
)
