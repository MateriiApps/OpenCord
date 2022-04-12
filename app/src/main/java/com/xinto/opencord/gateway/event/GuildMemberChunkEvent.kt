package com.xinto.opencord.gateway.event

import com.xinto.opencord.rest.dto.ApiGuildMemberChunk

data class GuildMemberChunkEvent(
    val data: ApiGuildMemberChunk
) : Event
