package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName

data class LoginUserSettingsResult(
    @SerializedName("locale") val locale: String,
    @SerializedName("theme") val theme: String,
)
