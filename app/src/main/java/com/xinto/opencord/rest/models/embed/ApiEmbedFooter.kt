package com.xinto.opencord.rest.models.embed

import com.xinto.opencord.domain.embed.DomainEmbedFooter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiEmbedFooter(
    @SerialName("text")
    val text: String,

    @SerialName("icon_url")
    val iconUrl: String? = null,

    @SerialName("proxy_icon_url")
    val proxyIconUrl: String? = null,
)

fun DomainEmbedFooter.toApi(): ApiEmbedFooter {
    return ApiEmbedFooter(
        text = text,
        iconUrl = iconUrl,
        proxyIconUrl = proxyIconUrl,
    )
}
