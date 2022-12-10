package com.xinto.opencord.domain.usersettings

import com.xinto.opencord.rest.dto.ApiGuildFolder

data class DomainGuildFolder(
    val id: Long?,
    val guildIds: List<Long>,
    val name: String?,
)

fun ApiGuildFolder.toDomain(): DomainGuildFolder {
    return DomainGuildFolder(
        id = id?.value,
        guildIds = guildIds.map { it.value },
        name = name,
    )
}
