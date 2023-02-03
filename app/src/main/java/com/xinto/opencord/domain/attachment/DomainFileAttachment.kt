package com.xinto.opencord.domain.attachment

import androidx.compose.runtime.Immutable

@Immutable
data class DomainFileAttachment(
    override val id: Long,
    override val filename: String,
    override val size: Int,
    override val url: String,
    override val proxyUrl: String,
    override val type: String?,
) : DomainAttachment
