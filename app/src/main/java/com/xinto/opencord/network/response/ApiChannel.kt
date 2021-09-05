package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.network.response.base.ApiResponse

data class ApiChannel(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: Int,
    @SerializedName("position") val position: Int,
    @SerializedName("parent_id") val parent_id: Long?,
    @SerializedName("nsfw") val nsfw: Boolean,
) : ApiResponse
