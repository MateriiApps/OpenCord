package com.xinto.opencord.gateway.event

import com.xinto.opencord.rest.dto.ApiMessage

data class MessageCreateEvent(
    val data: ApiMessage,
) : Event