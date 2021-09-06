package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.network.response.base.ApiResponse

data class ApiMessage(
    @SerializedName("id") val id: Long,
    @SerializedName("channel_id") val channelId: Long,
    @SerializedName("content") val content: String,
    @SerializedName("author") val author: ApiMessageAuthor,
) : ApiResponse