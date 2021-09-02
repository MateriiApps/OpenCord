package com.xinto.opencord.domain.model

data class DomainGuild(
    val id: Long,
    val name: String,
    val iconUrl: String,
    val channels: List<DomainChannel>
)
