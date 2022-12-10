package com.xinto.opencord.rest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiGuildMemberChunk(
    @SerialName("guild_id")
    val guildId: ApiSnowflake,

    @SerialName("members")
    val members: List<ApiGuildMember>,

    @SerialName("chunk_index")
    val chunkIndex: Int,

    @SerialName("chunk_count")
    val chunkCount: Int,
)
