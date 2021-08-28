package com.xinto.opencord.network.model

import com.google.gson.annotations.SerializedName

data class Guild(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("icon") val iconHash: String,
)