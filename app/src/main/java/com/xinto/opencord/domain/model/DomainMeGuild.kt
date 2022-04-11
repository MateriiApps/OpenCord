package com.xinto.opencord.domain.model

data class DomainMeGuild(
    val id: Long,
    val name: String,
    val iconUrl: String?,
    val iconText: String,
    val permissions: Long,
)