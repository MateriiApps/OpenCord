package com.xinto.opencord.domain.guild

import com.xinto.opencord.db.entity.guild.EntityGuild
import com.xinto.opencord.rest.dto.ApiGuild
import com.xinto.opencord.rest.service.DiscordCdnServiceImpl

data class DomainGuild(
    val id: Long,
    val name: String,
    val iconUrl: String?,
    val bannerUrl: String?,
    val premiumTier: Int,
    val premiumSubscriptionCount: Int,
) {
    val iconText = name
        .split(ICON_TEXT_REGEX)
        .map { it[0] }
        .joinToString()

    companion object {
        val ICON_TEXT_REGEX = """\s+""".toRegex()
    }
}

fun ApiGuild.toDomain(): DomainGuild {
    return DomainGuild(
        id = id.value,
        name = name,
        iconUrl = icon?.let { icon ->
            DiscordCdnServiceImpl.getGuildIconUrl(id.toString(), icon)
        },
        bannerUrl = banner?.let { banner ->
            DiscordCdnServiceImpl.getGuildBannerUrl(id.toString(), banner)
        },
        premiumTier = premiumTier,
        premiumSubscriptionCount = premiumSubscriptionCount ?: 0,
    )
}

fun EntityGuild.toDomain(): DomainGuild {
    return DomainGuild(
        id = id,
        name = name,
        iconUrl = icon?.let { icon ->
            DiscordCdnServiceImpl.getGuildIconUrl(id.toString(), icon)
        },
        bannerUrl = banner?.let { banner ->
            DiscordCdnServiceImpl.getGuildBannerUrl(id.toString(), banner)
        },
        premiumTier = premiumTier,
        premiumSubscriptionCount = premiumSubscriptionCount ?: 0,
    )
}
