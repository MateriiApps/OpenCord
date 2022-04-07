package com.xinto.opencord.domain.model

data class DomainGuildMemberChunk(
    val guildId: Long,
    val guildMembers: List<DomainGuildMember>,
    val chunkIndex: Int,
    val chunkCount: Int,
)
