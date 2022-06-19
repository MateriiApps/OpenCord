package com.xinto.opencord.rest.service

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DiscordApiService {
    suspend fun getMeGuilds(): List<ApiMeGuild>
    suspend fun getGuild(guildId: ULong): ApiGuild
    suspend fun getGuildChannels(guildId: ULong): List<ApiChannel>

    suspend fun getChannel(channelId: ULong): ApiChannel
    suspend fun getChannelMessages(channelId: ULong): List<ApiMessage>
    suspend fun getChannelPins(channelId: ULong): Map<ApiSnowflake, ApiMessage>

    suspend fun postChannelMessage(channelId: ULong, body: MessageBody)

    suspend fun getUserSettings(): ApiUserSettings
    suspend fun updateUserSettings(settings: ApiUserSettingsPartial): ApiUserSettings

    suspend fun startTyping(channelId: ULong)
}

class DiscordApiServiceImpl(
    gateway: DiscordGateway,
    private val client: HttpClient
) : DiscordApiService {
    private val cachedMeGuilds = mutableListOf<ApiMeGuild>()

    private val cachedGuildById = mutableMapOf<ULong, ApiGuild>()
    private val cachedChannelById = mutableMapOf<ULong, ApiChannel>()

    private val cachedChannelPins = mutableMapOf<ULong, MutableMap<ApiSnowflake, ApiMessage>>()

    private var cachedUserSettings: ApiUserSettings? = null

    override suspend fun getMeGuilds(): List<ApiMeGuild> {
        return withContext(Dispatchers.IO) {
            if (cachedMeGuilds.isEmpty()) {
                val url = getMeGuildsUrl()
                val response: List<ApiMeGuild> = client.get(url).body()
                cachedMeGuilds.addAll(response)
            }
            cachedMeGuilds
        }
    }

    override suspend fun getGuild(guildId: ULong): ApiGuild {
        return withContext(Dispatchers.IO) {
            if (cachedGuildById[guildId] == null) {
                val url = getGuildUrl(guildId)
                val response: ApiGuild = client.get(url).body()
                cachedGuildById[guildId] = response
            }
            cachedGuildById[guildId]!!
        }
    }

    override suspend fun getGuildChannels(guildId: ULong): List<ApiChannel> {
        return withContext(Dispatchers.IO) {
            val url = getGuildChannelsUrl(guildId)
            client.get(url).body()
        }
    }

    override suspend fun getChannel(channelId: ULong): ApiChannel {
        return withContext(Dispatchers.IO) {
            if (cachedChannelById[channelId] == null) {
                val url = getChannelUrl(channelId)
                cachedChannelById[channelId] = client.get(url).body()
            }
            cachedChannelById[channelId]!!
        }
    }

    override suspend fun getChannelMessages(channelId: ULong): List<ApiMessage> {
        return withContext(Dispatchers.IO) {
            val url = getChannelMessagesUrl(channelId)
            client.get(url).body()
        }
    }

    override suspend fun getChannelPins(channelId: ULong): Map<ApiSnowflake, ApiMessage> {
        return withContext(Dispatchers.IO) {
            if (cachedChannelPins[channelId] == null) {
                val url = getChannelPinsUrl(channelId)
                val response: List<ApiMessage> = client.get(url).body()
                cachedChannelPins[channelId] = response.associateBy { it.id }.toMutableMap()
            }
            cachedChannelPins[channelId]!!
        }
    }

    override suspend fun postChannelMessage(channelId: ULong, body: MessageBody) {
        withContext(Dispatchers.IO) {
            val url = getChannelMessagesUrl(channelId)
            client.post(url) {
                setBody(body)
            }
        }
    }

    override suspend fun getUserSettings(): ApiUserSettings {
        return withContext(Dispatchers.IO) {
            if (cachedUserSettings == null) {
                cachedUserSettings = client.get(getUserSettingsUrl()).body()
            }
            cachedUserSettings!!
        }
    }

    override suspend fun updateUserSettings(settings: ApiUserSettingsPartial): ApiUserSettings {
        return withContext(Dispatchers.IO) {
            client.patch(getUserSettingsUrl()) {
                setBody(settings)
            }.body<ApiUserSettings>().also {
                cachedUserSettings = it
            }
        }
    }

    override suspend fun startTyping(channelId: ULong) {
        withContext(Dispatchers.IO) {
            val url = getTypingUrl(channelId)
            client.post(url)
        }
    }

    private companion object {
        const val BASE = BuildConfig.URL_API

        fun getMeGuildsUrl(): String {
            return "$BASE/users/@me/guilds"
        }

        fun getGuildUrl(guildId: ULong): String {
            return "$BASE/guilds/$guildId"
        }

        fun getGuildChannelsUrl(guildId: ULong): String {
            val guildUrl = getGuildUrl(guildId)
            return "$guildUrl/channels"
        }

        fun getChannelUrl(channelId: ULong): String {
            return "$BASE/channels/$channelId"
        }

        fun getChannelMessagesUrl(channelId: ULong): String {
            val channelUrl = getChannelUrl(channelId)
            return "$channelUrl/messages"
        }

        fun getChannelPinsUrl(channelId: ULong): String {
            val channelUrl = getChannelUrl(channelId)
            return "$channelUrl/pins"
        }

        fun getUserSettingsUrl(): String {
            return "$BASE/users/@me/settings"
        }

        fun getTypingUrl(channelId: ULong): String {
            val channelUrl = getChannelUrl(channelId)
            return "$channelUrl/typing"
        }
    }
}
