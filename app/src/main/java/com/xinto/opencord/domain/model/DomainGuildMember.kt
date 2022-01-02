package com.xinto.opencord.domain.model

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.network.response.ApiGuildMember

data class DomainGuildMember(
    val user: DomainUser?,
    val nick: String? = null,
    val avatarUrl: String? = null,
) {

    companion object {

        fun fromApi(
            apiGuildMember: ApiGuildMember
        ) = with(apiGuildMember) {
            DomainGuildMember(
                user = if (user != null) DomainUser.fromApi(user) else null,
                nick = nick,
                avatarUrl = if (avatar != null)
                    "${BuildConfig.URL_CDN}/avatars/${user?.id}/$avatar.png"
                else
                    "${BuildConfig.URL_CDN}/embed/avatars/${user?.discriminator?.toInt()?.rem(5)}.png",
            )
        }
    }
}
