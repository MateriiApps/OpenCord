package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse

data class DomainMessage(
    val content: String
) : DomainResponse
