package com.xinto.opencord.rest.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiAttachment(
    @SerialName("id")
    val id: Long,

    @SerialName("filename")
    val filename: String,

    @SerialName("size")
    val size: Int,

    @SerialName("url")
    val url: String,

    @SerialName("proxy_url")
    val proxyUrl: String,

    @SerialName("width")
    val width: Int? = null,

    @SerialName("height")
    val height: Int? = null,

    @SerialName("content_type")
    val contentType: String = ""
)
