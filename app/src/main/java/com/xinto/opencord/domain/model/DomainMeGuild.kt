package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse

data class DomainMeGuild(
    val id: Long,
    val name: String,
    val iconUrl: String
) : DomainResponse
