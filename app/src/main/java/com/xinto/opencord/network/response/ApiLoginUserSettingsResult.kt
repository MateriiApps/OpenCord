package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName

data class ApiLoginUserSettingsResult(
    @SerializedName("locale") val locale: String,
    @SerializedName("theme") val theme: String,
)
