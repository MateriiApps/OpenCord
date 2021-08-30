package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName

data class CaptchaResponse(
    @SerializedName("captcha_sitekey") val captcha_sitekey: String?
)
