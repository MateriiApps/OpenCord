package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse
import com.xinto.opencord.network.response.ApiGuild
import com.xinto.opencord.network.util.discordCdnUrl

data class DomainGuild(
    val id: Long,
    val name: String,
    val iconUrl: String?,
    val bannerUrl: String?,
) : DomainResponse {

    companion object {

        fun fromApi(
            apiGuild: ApiGuild
        ) = with(apiGuild) {
            DomainGuild(
                id = id.toLong(),
                name = name,
                iconUrl = if (icon != null) "$discordCdnUrl/icons/$id/$icon" else null,
                bannerUrl = if (banner != null) "$discordCdnUrl/banners/$id/$banner" else null,
            )
        }

    }

}
