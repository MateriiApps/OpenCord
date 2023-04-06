package com.xinto.opencord.domain.embed

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.xinto.opencord.db.entity.message.EntityEmbed
import com.xinto.opencord.rest.models.ApiColor
import com.xinto.opencord.rest.models.embed.ApiEmbed
import com.xinto.opencord.rest.models.embed.ApiEmbedAuthor
import com.xinto.opencord.rest.models.embed.ApiEmbedField
import com.xinto.opencord.rest.models.embed.ApiEmbedMedia
import com.xinto.opencord.rest.models.toColor
import com.xinto.opencord.ui.util.toUnsafeImmutableList
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Instant

@Immutable
data class DomainEmbed(
    val title: String?,
    val description: String?,
    val url: String?,
    val color: Color?,
    val timestamp: Instant?,
    val author: ApiEmbedAuthor?,
    val image: ApiEmbedMedia?,
    val fields: ImmutableList<ApiEmbedField>?,
)

fun ApiEmbed.toDomain(): DomainEmbed {
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.toColor(),
        timestamp = timestamp,
        author = author,
        image = image,
        fields = fields?.toUnsafeImmutableList(),
    )
}

fun EntityEmbed.toDomain(): DomainEmbed {
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.let { ApiColor(it).toColor() },
        timestamp = timestamp?.let { Instant.fromEpochMilliseconds(it) },
        author = author,
        image = image,
        fields = fields?.toUnsafeImmutableList(),
    )
}
