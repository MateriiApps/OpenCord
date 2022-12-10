package com.xinto.opencord.domain.embed

import com.xinto.opencord.rest.models.embed.ApiEmbedField

data class DomainEmbedField(
    val name: String,
    val value: String,
)

fun ApiEmbedField.toDomain(): DomainEmbedField {
    return DomainEmbedField(
        name = name,
        value = value,
    )
}
