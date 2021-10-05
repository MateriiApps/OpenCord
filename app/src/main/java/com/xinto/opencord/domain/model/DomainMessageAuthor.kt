package com.xinto.opencord.domain.model

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.network.response.ApiMessageAuthor

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
                    "${BuildConfig.URL_CDN}/avatars/$id/$avatar.png"
                else
                    "${BuildConfig.URL_CDN}/embed/avatars/${discriminator.toInt() % 5}.png"
            )
        }

    }

}
