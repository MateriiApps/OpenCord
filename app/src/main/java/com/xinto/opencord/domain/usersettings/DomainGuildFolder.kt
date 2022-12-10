package com.xinto.opencord.domain.usersettings

import androidx.compose.runtime.Immutable
import com.xinto.opencord.rest.dto.ApiGuildFolder

@Immutable
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
