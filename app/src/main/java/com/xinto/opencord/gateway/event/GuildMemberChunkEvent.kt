package com.xinto.opencord.gateway.event

import com.xinto.opencord.rest.dto.ApiGuildMemberChunk

data class GuildMemberChunkEvent(
    override val data: ApiGuildMemberChunk
) : Event<ApiGuildMemberChunk>
