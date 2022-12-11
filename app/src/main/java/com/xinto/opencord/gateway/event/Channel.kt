package com.xinto.opencord.gateway.event

import com.xinto.opencord.rest.models.ApiChannel

data class ChannelCreateEvent(
    val data: ApiChannel,
) : Event

data class ChannelUpdateEvent(
    val data: ApiChannel,
) : Event

data class ChannelDeleteEvent(
    val data: ApiChannel,
) : Event
