package com.xinto.opencord.network.response.base

import com.google.gson.annotations.SerializedName

data class ApiAttachment(
    @SerializedName("id") val id: Long,
    @SerializedName("filename") val filename: String,
    @SerializedName("size") val size: Int,
    @SerializedName("url") val url: String,
    @SerializedName("proxy_url") val proxyUrl: String,
    @SerializedName("width") val width: Int? = null,
    @SerializedName("height") val height: Int? = null,
    @SerializedName("content_type") val contentType: String = ""
)
