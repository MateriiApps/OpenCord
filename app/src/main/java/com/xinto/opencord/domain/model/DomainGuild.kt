package com.xinto.opencord.domain.model

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.model.base.DomainResponse
import com.xinto.opencord.network.response.ApiGuild

data class DomainGuild(
    val id: Long,
    val name: String,
    val iconUrl: String?,
    val bannerUrl: String?,
) : DomainResponse {

    companion object {

        fun fromApi(
            apiGuild: ApiGuild,
        ) = with(apiGuild) {
            DomainGuild(
                id = id.toLong(),
                name = name,
                iconUrl = if (icon != null) "${BuildConfig.URL_CDN}/icons/$id/$icon" else null,
                bannerUrl = if (banner != null) "${BuildConfig.URL_CDN}/banners/$id/$banner" else null,
            )
        }

    }

}
