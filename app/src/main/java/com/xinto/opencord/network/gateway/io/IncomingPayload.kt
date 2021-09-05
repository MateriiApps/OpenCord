package com.xinto.opencord.network.gateway.io

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class IncomingPayload(
    @SerializedName("op") val op: Int,
    @SerializedName("d") val d: JsonElement?,
    @SerializedName("s") val s: Int?,
    @SerializedName("t") val t: String?,
)