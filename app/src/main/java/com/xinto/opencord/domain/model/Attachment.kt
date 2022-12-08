package com.xinto.opencord.domain.model

sealed interface DomainAttachment {
    val id: Long
    val filename: String
    val size: Int
    val url: String
    val proxyUrl: String

    data class Picture(
        override val id: Long,
        override val filename: String,
        override val size: Int,
        override val url: String,
        override val proxyUrl: String,
        val width: Int,
        val height: Int,
    ) : DomainAttachment

    data class Video(
        override val id: Long,
        override val filename: String,
        override val size: Int,
        override val url: String,
        override val proxyUrl: String,
        val width: Int,
        val height: Int,
    ) : DomainAttachment

    data class File(
        override val id: Long,
        override val filename: String,
        override val size: Int,
        override val url: String,
        override val proxyUrl: String,
    ) : DomainAttachment
}
