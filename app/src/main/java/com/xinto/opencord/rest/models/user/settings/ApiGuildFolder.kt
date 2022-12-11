package com.xinto.opencord.rest.models.user.settings

import com.xinto.opencord.domain.usersettings.DomainGuildFolder
import com.xinto.opencord.rest.models.ApiSnowflake
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiGuildFolder(
    @SerialName("guild_ids")
    val guildIds: List<ApiSnowflake>,

    @SerialName("id")
    val id: ApiSnowflake? = null,

    @SerialName("name")
    val name: String? = null,
)

fun DomainGuildFolder.toApi(): ApiGuildFolder {
    return ApiGuildFolder(
        id = id?.let { ApiSnowflake(it) },
        guildIds = guildIds.map { ApiSnowflake(it) },
        name = name,
    )
}
