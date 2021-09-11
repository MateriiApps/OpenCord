package com.xinto.opencord.network.gateway.data.incoming

import com.google.gson.annotations.SerializedName

data class Heartbeat(
    @SerializedName("heartbeat_interval") val heartbeat_interval: Long,
)
