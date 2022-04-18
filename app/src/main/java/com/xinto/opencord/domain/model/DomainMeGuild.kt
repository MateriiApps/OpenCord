package com.xinto.opencord.domain.model

data class DomainMeGuild(
    val id: ULong,
    val name: String,
    val iconUrl: String?,
    val permissions: List<DomainPermission>,
) {
    val iconText
        get() = name.split("""\s+""".toRegex()).map { it[0] }.joinToString()
}