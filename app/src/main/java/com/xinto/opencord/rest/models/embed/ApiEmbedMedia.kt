package com.xinto.opencord.rest.models.embed

import com.xinto.opencord.util.queryParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiEmbedMedia(
    @SerialName("url")
    val url: String,

    @SerialName("proxy_url")
    val proxyUrl: String? = null,

    @SerialName("height")
    val height: Int? = null,

    @SerialName("width")
    val width: Int? = null,
) {
    val displayUrl: String by lazy {
        if (proxyUrl != null) {
            val params = queryParameters(2) {
                width?.also { append("width", it.toString()) }
                height?.also { append("height", it.toString()) }
            }
            "$proxyUrl$params"
        } else {
            url
        }
    }
}
