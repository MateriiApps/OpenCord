package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("token") val token: String,
    @SerializedName("mfa") val mfa: Boolean,
    @SerializedName("userSettings") val userSettings: LoginUserSettingsResult
)