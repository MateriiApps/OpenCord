package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.network.response.base.ApiResponse

data class ApiMember(
    @SerializedName("nick") val nick: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
) : ApiResponse