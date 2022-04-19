package com.xinto.opencord.gateway.event

import com.xinto.opencord.rest.dto.ApiGuild

data class GuildCreateEvent(
    val data: ApiGuild
): Event

data class GuildUpdateEvent(
    val data: ApiGuild
): Event

