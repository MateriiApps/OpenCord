package com.xinto.opencord.domain.model

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.rest.dto.ApiUser

data class DomainUser(
    val id: Long,
    val username: String,
    val discriminator: String,
    val avatarUrl: String?,
    val bot: Boolean = false,
) {

    val tag get() = "$username#$discriminator"

    companion object {

        fun fromApi(
            apiUser: ApiUser
        ) = with(apiUser) {
            DomainUser(
                id = id,
                username = username,
                discriminator = discriminator,
                avatarUrl =
                if (avatar != null)
                    "${BuildConfig.URL_CDN}/avatars/$id/$avatar.png"
                else
                    "${BuildConfig.URL_CDN}/embed/avatars/${discriminator.toInt() % 5}.png",
                bot = bot,
            )
        }
    }
}