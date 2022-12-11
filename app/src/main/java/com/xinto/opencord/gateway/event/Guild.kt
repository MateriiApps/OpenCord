package com.xinto.opencord.gateway.event

import com.xinto.opencord.gateway.dto.GuildDeleteData
import com.xinto.opencord.rest.models.ApiGuild

data class GuildCreateEvent(
    val data: ApiGuild
) : Event

data class GuildUpdateEvent(
    val data: ApiGuild
) : Event

data class GuildDeleteEvent(
    val data: GuildDeleteData
) : Event
