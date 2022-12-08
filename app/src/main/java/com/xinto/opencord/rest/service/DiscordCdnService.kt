package com.xinto.opencord.rest.service

import com.xinto.opencord.BuildConfig

interface DiscordCdnService

class DiscordCdnServiceImpl : DiscordCdnService {
    companion object {
        private const val BASE = BuildConfig.URL_CDN

        fun getDefaultAvatarUrl(avatar: Int): String {
            return "$BASE/embed/avatars/$avatar.png"
        }

        fun getUserAvatarUrl(userId: String, avatarHash: String): String {
            return "$BASE/avatars/${userId}/$avatarHash.png"
        }

        fun getGuildIconUrl(guildId: String, iconHash: String): String {
            return "$BASE/icons/$guildId/$iconHash"
        }

        fun getGuildBannerUrl(guildId: String, iconHash: String): String {
            return "$BASE/banners/$guildId/$iconHash"
        }
    }
}
