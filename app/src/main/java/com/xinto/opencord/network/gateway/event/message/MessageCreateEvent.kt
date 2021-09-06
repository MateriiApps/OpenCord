package com.xinto.opencord.network.gateway.event.message

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.network.gateway.event.Event
import com.xinto.opencord.network.response.ApiMessage

data class MessageCreateEvent(
    val message: ApiMessage
) : Event
