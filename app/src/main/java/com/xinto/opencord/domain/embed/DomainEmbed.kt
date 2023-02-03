package com.xinto.opencord.domain.embed

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.xinto.opencord.db.entity.message.EntityEmbed
import com.xinto.opencord.rest.models.embed.ApiEmbed
import com.xinto.opencord.rest.models.embed.ApiEmbedField
import kotlinx.datetime.Instant

@Immutable
data class DomainEmbed(
    val title: String?,
    val description: String?,
    val url: String?,
    val color: Color?,
    val timestamp: Instant?,
    val author: String?,
    val fields: List<ApiEmbedField>?
)

fun ApiEmbed.toDomain(): DomainEmbed {
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.let {
            Color(red = it.red, green = it.green, blue = it.blue)
        },
        timestamp = timestamp,
        author = author?.name,
        fields = fields,
    )
}

fun EntityEmbed.toDomain(): DomainEmbed {
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.let { Color(it) },
        timestamp = timestamp?.let { Instant.fromEpochMilliseconds(it) },
        author = authorName,
        fields = fields,
    )
}
