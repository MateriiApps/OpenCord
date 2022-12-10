package com.xinto.opencord.domain.embed

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.xinto.opencord.db.entity.message.EntityEmbed
import com.xinto.opencord.rest.dto.ApiEmbed

@Immutable
data class DomainEmbed(
    val title: String?,
    val description: String?,
    val url: String?,
    val color: Color?,
    val author: DomainEmbedAuthor?,
    val fields: List<DomainEmbedField>?
)

fun ApiEmbed.toDomain(): DomainEmbed {
    val domainAuthor = author?.toDomain()
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.let {
            Color(red = it.red, green = it.green, blue = it.blue)
        },
        author = domainAuthor,
        fields = fields?.map { it.toDomain() },
    )
}

fun EntityEmbed.toDomain(): DomainEmbed {
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.let { Color(it) },
        author = authorName?.let { DomainEmbedAuthor(it) },
        fields = fields?.map { it.toDomain() },
    )
}
