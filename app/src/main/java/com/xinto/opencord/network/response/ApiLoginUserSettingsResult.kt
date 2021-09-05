package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.network.response.base.ApiResponse

data class ApiLoginUserSettingsResult(
    @SerializedName("locale") val locale: String,
    @SerializedName("theme") val theme: String,
) : ApiResponse
