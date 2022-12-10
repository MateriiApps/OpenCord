package com.xinto.opencord.domain.embed

import androidx.compose.runtime.Immutable

@Immutable
data class DomainEmbedThumbnail(
    val url: String,
    val proxyUrl: String,
    val height: Int?,
    val width: Int?
)
