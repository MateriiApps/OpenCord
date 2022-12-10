package com.xinto.opencord.domain.embed

import com.xinto.opencord.rest.dto.ApiEmbedAuthor

data class DomainEmbedAuthor(
    val name: String,
)

fun ApiEmbedAuthor.toDomain(): DomainEmbedAuthor {
    return DomainEmbedAuthor(
        name = name,
    )
}
