package com.xinto.opencord.rest.service

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.UserSettingsUpdateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.dto.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DiscordApiService {
    suspend fun getMeGuilds(): List<ApiMeGuild>
    suspend fun getGuild(guildId: Long): ApiGuild
    suspend fun getGuildChannels(guildId: Long): Map<ApiSnowflake, ApiChannel>

    suspend fun getChannel(channelId: Long): ApiChannel
    suspend fun getChannelPins(channelId: Long): Map<ApiSnowflake, ApiMessage>
    suspend fun getChannelMessages(
        channelId: Long,
        limit: Long,
        before: Long? = null,
        after: Long? = null,
        around: Long? = null,
    ): Map<ApiSnowflake, ApiMessage>

    suspend fun postChannelMessage(channelId: Long, body: MessageBody)

    suspend fun getUserSettings(): ApiUserSettings
    suspend fun updateUserSettings(settings: ApiUserSettingsPartial): ApiUserSettings

    suspend fun startTyping(channelId: Long)
}

class DiscordApiServiceImpl(
    gateway: DiscordGateway,
    private val client: HttpClient
) : DiscordApiService {
    private val cachedMeGuilds = mutableListOf<ApiMeGuild>()

    private val cachedGuildById = mutableMapOf<Long, ApiGuild>()
    private val cachedChannelById = mutableMapOf<Long, ApiChannel>()

    private val cachedGuildChannels = mutableMapOf<Long, MutableMap<ApiSnowflake, ApiChannel>>()

    //    private val cachedChannelMessages = mutableMapOf<Long, MutableMap<ApiSnowflake, ApiMessage>>()
    private val cachedChannelPins = mutableMapOf<Long, MutableMap<ApiSnowflake, ApiMessage>>()

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

    override suspend fun getGuild(guildId: Long): ApiGuild {
        return withContext(Dispatchers.IO) {
            if (cachedGuildById[guildId] == null) {
                val url = getGuildUrl(guildId)
                val response: ApiGuild = client.get(url).body()
                cachedGuildById[guildId] = response
            }
            cachedGuildById[guildId]!!
        }
    }

    override suspend fun getGuildChannels(guildId: Long): Map<ApiSnowflake, ApiChannel> {
        return withContext(Dispatchers.IO) {
            if (cachedGuildChannels[guildId] == null) {
                val url = getGuildChannelsUrl(guildId)
                val response: List<ApiChannel> = client.get(url).body()
                cachedGuildChannels[guildId] = response.associateBy { it.id }.toMutableMap()
            }
            cachedGuildChannels[guildId]!!
        }
    }

    override suspend fun getChannel(channelId: Long): ApiChannel {
        return withContext(Dispatchers.IO) {
            if (cachedChannelById[channelId] == null) {
                val url = getChannelUrl(channelId)
                cachedChannelById[channelId] = client.get(url).body()
            }
            cachedChannelById[channelId]!!
        }
    }

    override suspend fun getChannelMessages(
        channelId: Long,
        limit: Long,
        before: Long?,
        after: Long?,
        around: Long?,
    ): Map<ApiSnowflake, ApiMessage> {
        return withContext(Dispatchers.IO) {
            val messages = client
                .get(getChannelMessagesUrl(channelId, before, after, around))
                .body<List<ApiMessage>>()

            // TODO: remove this map
            messages.associateBy { it.id }.toMutableMap()
        }
    }

    override suspend fun getChannelPins(channelId: Long): Map<ApiSnowflake, ApiMessage> {
        return withContext(Dispatchers.IO) {
            if (cachedChannelPins[channelId] == null) {
                val url = getChannelPinsUrl(channelId)
                val response: List<ApiMessage> = client.get(url).body()
                cachedChannelPins[channelId] = response.associateBy { it.id }.toMutableMap()
            }
            cachedChannelPins[channelId]!!
        }
    }

    override suspend fun postChannelMessage(channelId: Long, body: MessageBody) {
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

    override suspend fun startTyping(channelId: Long) {
        withContext(Dispatchers.IO) {
            val url = getTypingUrl(channelId)
            client.post(url)
        }
    }

    init {
        gateway.onEvent<UserSettingsUpdateEvent> {
            cachedUserSettings = cachedUserSettings?.merge(it.data)
        }
    }

    private companion object {
        const val BASE = BuildConfig.URL_API

        fun getMeGuildsUrl(): String {
            return "$BASE/users/@me/guilds"
        }

        fun getGuildUrl(guildId: Long): String {
            return "$BASE/guilds/$guildId"
        }

        fun getGuildChannelsUrl(guildId: Long): String {
            val guildUrl = getGuildUrl(guildId)
            return "$guildUrl/channels"
        }

        fun getChannelUrl(channelId: Long): String {
            return "$BASE/channels/$channelId"
        }

        fun getChannelMessagesUrl(
            channelId: Long,
            limit: Long? = null,
            before: Long? = null,
            after: Long? = null,
            around: Long? = null,
        ): String {
            val channelUrl = getChannelUrl(channelId) + "/messages"

            val parameters = ParametersBuilder(4).apply {
                before?.let { append("before", it.toString()) }
                after?.let { append("after", it.toString()) }
                around?.let { append("around", it.toString()) }
                limit?.let { append("limit", limit.toString()) }
            }

            return channelUrl + parameters.build().formUrlEncode()
        }

        fun getChannelPinsUrl(channelId: Long): String {
            val channelUrl = getChannelUrl(channelId)
            return "$channelUrl/pins"
        }

        fun getUserSettingsUrl(): String {
            return "$BASE/users/@me/settings"
        }

        fun getTypingUrl(channelId: Long): String {
            val channelUrl = getChannelUrl(channelId)
            return "$channelUrl/typing"
        }
    }
}
