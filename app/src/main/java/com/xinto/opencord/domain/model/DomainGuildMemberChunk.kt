package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse
import com.xinto.opencord.network.response.ApiGuildMemberChunk

data class DomainGuildMemberChunk(
    val guildId: Long,
    val guildMembers: List<DomainGuildMember>,
    val chunkIndex: Int,
    val chunkCount: Int,
) : DomainResponse {

    companion object {

        fun fromApi(
            apiGuildMemberChunk: ApiGuildMemberChunk
        ) = with(apiGuildMemberChunk) {
            DomainGuildMemberChunk(
                guildId = guildId,
                guildMembers = members.map { DomainGuildMember.fromApi(it) },
                chunkIndex = chunkIndex,
                chunkCount = chunkCount,
            )
        }
    }

}
