package com.xinto.opencord.rest.models.embed

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiEmbedAuthor(
    @SerialName("name")
    val name: String,

    @SerialName("url")
    val url: String? = null,

    @SerialName("icon_url")
    val iconUrl: String? = null,

    @SerialName("proxy_icon_url")
    val proxyIconUrl: String? = null,
)
