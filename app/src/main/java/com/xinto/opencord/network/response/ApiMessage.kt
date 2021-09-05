package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.network.response.base.ApiResponse

data class ApiMessage(
    @SerializedName("content") val content: String
) : ApiResponse