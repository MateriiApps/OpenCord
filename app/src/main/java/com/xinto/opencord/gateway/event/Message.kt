package com.xinto.opencord.gateway.event

import com.xinto.opencord.gateway.dto.MessageDeleteData
import com.xinto.opencord.rest.dto.ApiMessagePartial
import com.xinto.opencord.rest.models.message.ApiMessage

data class MessageCreateEvent(
    val data: ApiMessage,
) : Event

data class MessageUpdateEvent(
    val data: ApiMessagePartial,
) : Event

data class MessageDeleteEvent(
    val data: MessageDeleteData,
) : Event
