package com.xinto.opencord.domain.guild

import androidx.compose.runtime.Immutable
import com.xinto.opencord.db.entity.guild.EntityGuild
import com.xinto.opencord.rest.models.ApiGuild
import com.xinto.opencord.rest.service.DiscordCdnServiceImpl

@Immutable
data class DomainGuild(
    val id: Long,
    val name: String,
    val iconUrl: String?,
    val bannerUrl: String?,
    val premiumTier: Int,
    val premiumSubscriptionCount: Int,
) {
    val iconText by lazy {
        ICON_TEXT_REGEX.findAll(name)
            .joinToString("") { it.groupValues[1] }
            .take(4)
            .takeIf(String::isNotEmpty)
            ?: name.firstOrNull { !it.isWhitespace() }.toString()
    }

    companion object {
        val ICON_TEXT_REGEX = """(?:^|\s)([\P{Cc}\P{Cs}\P{Cn}])""".toRegex()
    }
}

fun ApiGuild.toDomain(): DomainGuild {
    return DomainGuild(
        id = id.value,
        name = name,
        iconUrl = icon?.let { icon ->
            DiscordCdnServiceImpl.getGuildIconUrl(id.value, icon)
        },
        bannerUrl = banner?.let { banner ->
            DiscordCdnServiceImpl.getGuildBannerUrl(id.value, banner)
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
            DiscordCdnServiceImpl.getGuildIconUrl(id, icon)
        },
        bannerUrl = banner?.let { banner ->
            DiscordCdnServiceImpl.getGuildBannerUrl(id, banner)
        },
        premiumTier = premiumTier,
        premiumSubscriptionCount = premiumSubscriptionCount ?: 0,
    )
}
