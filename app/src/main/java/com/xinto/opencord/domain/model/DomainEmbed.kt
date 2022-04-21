package com.xinto.opencord.domain.model

import androidx.compose.ui.graphics.Color

data class DomainEmbed(
    val title: String?,
    val description: String?,
    val url: String?,
    val color: Color?,
    val author: DomainEmbedAuthor?,
    val fields: List<DomainEmbedField>?
)

data class DomainEmbedAuthor(
    val name: String,
)

data class DomainEmbedThumbnail(
    val url: String,
    val proxyUrl: String,
    val height: Int?,
    val width: Int?
)

data class DomainEmbedFooter(
    val text: String
)

data class DomainEmbedField(
    val name: String,
    val value: String,
)
