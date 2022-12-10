package com.xinto.opencord.domain.embed

import androidx.compose.runtime.Immutable
import com.xinto.opencord.rest.models.embed.ApiEmbedAuthor

@Immutable
data class DomainEmbedAuthor(
    val name: String,
)

fun ApiEmbedAuthor.toDomain(): DomainEmbedAuthor {
    return DomainEmbedAuthor(
        name = name,
    )
}
