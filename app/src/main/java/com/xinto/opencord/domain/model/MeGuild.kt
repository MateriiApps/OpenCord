package com.xinto.opencord.domain.model

data class DomainMeGuild(
    val id: Long,
    val name: String,
    val iconUrl: String?,
    val permissions: List<DomainPermission>,
) {
    val iconText = name.split("""\s+""".toRegex()).map { it[0] }.joinToString()
}