package com.xinto.opencord.network.gateway.event.member

import com.xinto.opencord.network.gateway.event.Event
import com.xinto.opencord.network.response.ApiGuildMemberChunk

data class GuildMemberChunkEvent(
    val apiGuildMemberChunk: ApiGuildMemberChunk
) : Event
