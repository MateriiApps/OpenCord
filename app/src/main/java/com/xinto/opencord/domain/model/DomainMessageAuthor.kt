package com.xinto.opencord.domain.model

import com.xinto.opencord.network.response.ApiMessageAuthor
import com.xinto.opencord.network.util.discordCdnUrl

data class DomainMessageAuthor(
    val userId: Long,
    val username: String,
    val discriminator: String,
    val avatarUrl: String,
) {

    companion object {

        fun fromApi(
            apiMessageAuthor: ApiMessageAuthor,
        ) = with(apiMessageAuthor) {
            DomainMessageAuthor(
                userId = id,
                username = username,
                discriminator = discriminator,
                avatarUrl =
                if (avatar != null)
                    "${discordCdnUrl}/avatars/$id/$avatar.png"
                else
                    "${discordCdnUrl}/embed/avatars/${discriminator.toInt() % 5}.png"
            )
        }

    }

}
