package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.network.response.base.ApiResponse

data class CaptchaResponse(
    @SerializedName("captcha_sitekey") val captcha_sitekey: String?
) : ApiResponse
