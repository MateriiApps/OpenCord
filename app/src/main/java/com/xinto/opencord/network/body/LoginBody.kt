package com.xinto.opencord.network.body

import com.google.gson.annotations.SerializedName

data class LoginBody(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String,
    @SerializedName("captcha_key") val captchaKey: String? = null,
    @SerializedName("undelete") val undelete: Boolean = false,
    @SerializedName("login_source") val loginSource: String? = null,
)
