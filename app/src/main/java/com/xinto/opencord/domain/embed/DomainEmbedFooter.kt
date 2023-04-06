package com.xinto.opencord.domain.embed

import androidx.compose.runtime.Immutable
import com.xinto.opencord.rest.models.embed.ApiEmbedFooter
import com.xinto.opencord.util.Timestamp
import kotlinx.datetime.Instant

@Immutable
data class DomainEmbedFooter(
    val text: String,
    val timestamp: Instant?,
    val iconUrl: String?,
    val proxyIconUrl: String?,
) {
    val formattedTimestamp: String? by lazy {
        timestamp?.let { Timestamp.getFormattedTimestamp(it) }
    }

    val displayUrl: String? by lazy {
        if (proxyIconUrl != null) {
            "$proxyIconUrl?width=64&height=64"
        } else {
            iconUrl
        }
    }
}

fun ApiEmbedFooter.toDomain(timestamp: Instant?): DomainEmbedFooter {
    return DomainEmbedFooter(
        text = text,
        timestamp = timestamp,
        iconUrl = iconUrl,
        proxyIconUrl = proxyIconUrl,
    )
}
