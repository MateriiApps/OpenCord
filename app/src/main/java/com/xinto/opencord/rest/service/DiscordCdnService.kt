package com.xinto.opencord.rest.service

import com.xinto.opencord.BuildConfig

interface DiscordCdnService

class DiscordCdnServiceImpl : DiscordCdnService {
    companion object {
        private const val BASE = BuildConfig.URL_CDN

        fun getDefaultAvatarUrl(avatar: Int): String {
            return "$BASE/embed/avatars/$avatar.png"
        }

        fun getUserAvatarUrl(userId: Long, avatarHash: String): String {
            return "$BASE/avatars/${userId}/$avatarHash.webp?size=100"
        }

        fun getGuildIconUrl(guildId: Long, iconHash: String): String {
            return "$BASE/icons/$guildId/$iconHash.webp?size=128"
        }

        fun getGuildBannerUrl(guildId: Long, iconHash: String): String {
            return "$BASE/banners/$guildId/$iconHash.webp?size=1024"
        }
    }
}
