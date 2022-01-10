package com.xinto.opencord.domain.model

import com.xinto.opencord.network.response.ApiAttachment

sealed class DomainAttachment {

    abstract val id: Long
    abstract val filename: String
    abstract val size: Int
    abstract val url: String
    abstract val proxyUrl: String

    data class Picture(
        override val id: Long,
        override val filename: String,
        override val size: Int,
        override val url: String,
        override val proxyUrl: String,
        val width: Int,
        val height: Int,
    ) : DomainAttachment()

    data class Video(
        override val id: Long,
        override val filename: String,
        override val size: Int,
        override val url: String,
        override val proxyUrl: String,
        val width: Int,
        val height: Int,
    ) : DomainAttachment()

    data class File(
        override val id: Long,
        override val filename: String,
        override val size: Int,
        override val url: String,
        override val proxyUrl: String,
    ) : DomainAttachment()

    companion object {

        fun fromApi(
            apiAttachment: ApiAttachment
        ) = with(apiAttachment) {
            if (contentType.isNotEmpty()) {
                when (contentType) {
                    "video/mp4" -> Video(
                        id = id,
                        filename = filename,
                        size = size,
                        url = url,
                        proxyUrl = proxyUrl,
                        width = width ?: 0,
                        height = height ?: 0
                    )
                    else -> Picture(
                        id = id,
                        filename = filename,
                        size = size,
                        url = url,
                        proxyUrl = proxyUrl,
                        width = width ?: 0,
                        height = height ?: 0
                    )
                }
            } else {
                File(
                    id = id,
                    filename = filename,
                    size = size,
                    url = url,
                    proxyUrl = proxyUrl,
                )
            }
        }
    }
}

