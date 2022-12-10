package com.xinto.opencord.domain.attachment

data class DomainVideoAttachment(
    override val id: Long,
    override val filename: String,
    override val size: Int,
    override val url: String,
    override val proxyUrl: String,
    val width: Int,
    val height: Int,
) : DomainAttachment
