package com.xinto.opencord.gateway.event

import com.xinto.opencord.gateway.dto.MessageDeleteData
import com.xinto.opencord.rest.dto.ApiMessage

data class MessageCreateEvent(
    val data: ApiMessage,
) : Event

data class MessageUpdateEvent(
    val data: ApiMessage,
) : Event

data class MessageDeleteEvent(
    val data: MessageDeleteData,
) : Event