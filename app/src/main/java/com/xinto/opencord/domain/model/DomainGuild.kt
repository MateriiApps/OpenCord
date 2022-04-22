package com.xinto.opencord.domain.model

data class DomainGuild(
    val id: ULong,
    val name: String,
    val iconUrl: String?,
    val bannerUrl: String?,
    val permissions: List<DomainPermission>,
) {
    val iconText = name.split("""\s+""".toRegex()).map { it[0] }.joinToString()
}
