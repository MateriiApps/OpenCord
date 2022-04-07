package com.xinto.opencord.domain.model

data class DomainGuildMember(
    val user: DomainUser?,
    val nick: String? = null,
    val avatarUrl: String? = null,
)
