package com.xinto.opencord.network.response

import com.google.gson.annotations.SerializedName
import com.xinto.opencord.network.response.base.ApiResponse

data class ApiGuildMemberChunk(
    @SerializedName("guild_id") val guildId: Long,
    @SerializedName("members") val members: List<ApiGuildMember>,
    @SerializedName("chunk_index") val chunkIndex: Int,
    @SerializedName("chunk_count") val chunkCount: Int,
) : ApiResponse
