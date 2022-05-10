package com.xinto.opencord.domain.model

data class DomainGuildMemberChunk(
    val guildId: ULong,
    val guildMembers: List<DomainGuildMember>,
    val chunkIndex: Int,
    val chunkCount: Int,
)
