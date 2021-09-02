package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName

data class ApiMessage(
    @SerializedName("content") val content: String
)