package com.xinto.opencord.domain.model

import com.xinto.opencord.domain.model.base.DomainResponse
import com.xinto.opencord.network.response.ApiMeGuild

data class DomainMeGuild(
    val id: Long,
    val name: String,
    val iconUrl: String,
    val permissions: Long,
) : DomainResponse {

    companion object {

        fun fromApi(
            apiMeGuild: ApiMeGuild
        ) = with(apiMeGuild) {
            DomainMeGuild(
                id = id.toLong(),
                name = name,
                iconUrl = "https://cdn.discordapp.com/icons/$id/$icon",
                permissions = permissions.toLong()
            )
        }

    }

}
