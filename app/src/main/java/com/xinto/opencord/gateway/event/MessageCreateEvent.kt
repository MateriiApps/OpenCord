package com.xinto.opencord.gateway.event

import com.xinto.opencord.rest.dto.ApiMessage

data class MessageCreateEvent(
    override val data: ApiMessage,
) : Event<ApiMessage>
