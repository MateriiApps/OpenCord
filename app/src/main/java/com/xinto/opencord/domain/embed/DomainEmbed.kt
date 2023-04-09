package com.xinto.opencord.domain.embed

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.xinto.opencord.db.entity.message.EntityEmbed
import com.xinto.opencord.rest.models.ApiColor
import com.xinto.opencord.rest.models.embed.*
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
    val author: ApiEmbedAuthor?,
    val footer: DomainEmbedFooter?,
    val thumbnail: ApiEmbedMedia?,
    val image: ApiEmbedMedia?,
    val video: ApiEmbedMedia?,
    val fields: ImmutableList<ApiEmbedField>?,
) {
    // Determines that this should be displayed like an attachment
    val isVideoOnlyEmbed: Boolean
        get() = video?.proxyUrl != null
}

fun ApiEmbed.toDomain(): DomainEmbed {
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.toColor(),
        author = author,
        footer = footer?.toDomain(timestamp),
        thumbnail = thumbnail,
        image = image,
        video = video,
        fields = fields?.toUnsafeImmutableList(),
    )
}

fun EntityEmbed.toDomain(): DomainEmbed {
    return DomainEmbed(
        title = title,
        description = description,
        url = url,
        color = color?.let { ApiColor(it).toColor() },
        author = author,
        footer = footer?.toDomain(
            timestamp = timestamp?.let { Instant.fromEpochMilliseconds(it) },
        ),
        thumbnail = thumbnail,
        image = image,
        video = video,
        fields = fields?.toUnsafeImmutableList(),
    )
}
