package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName

data class ApiUser(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("discriminator") val discriminator: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("bot") val bot: Boolean = false,
)