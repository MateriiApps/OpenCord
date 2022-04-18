package com.xinto.opencord.rest.service

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.dto.ApiChannel
import com.xinto.opencord.rest.dto.ApiGuild
import com.xinto.opencord.rest.dto.ApiMeGuild
import com.xinto.opencord.rest.dto.ApiMessage
import com.xinto.opencord.util.ListMap
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

    suspend fun getChannelPins(channelId: ULong): List<ApiMessage>

    suspend fun postChannelMessage(channelId: ULong, body: MessageBody)

}

class DiscordApiServiceImpl(
    gateway: DiscordGateway,
    private val client: HttpClient
) : DiscordApiService {

    private val cachedMeGuilds = mutableListOf<ApiMeGuild>()

    private val cachedGuildById = mutableMapOf<ULong, ApiGuild>()
    private val cachedChannelById = mutableMapOf<ULong, ApiChannel>()

    private val cachedGuildChannels = ListMap<ULong, ApiChannel>()
    private val cachedChannelMessages = ListMap<ULong, ApiMessage>()
    private val cachedChannelPins = ListMap<ULong, ApiMessage>()

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
            if (cachedGuildChannels[guildId].isEmpty()) {
                val url = getGuildChannelsUrl(guildId)
                val response: List<ApiChannel> = client.get(url).body()
                cachedGuildChannels[guildId].addAll(response)
            }
            cachedGuildChannels[guildId]
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
            if (cachedChannelMessages[channelId].isEmpty()) {
                val url = getChannelMessagesUrl(channelId)
                val response: List<ApiMessage> = client.get(url).body()
                cachedChannelMessages[channelId].addAll(response)
            }
            cachedChannelMessages[channelId]
        }
    }

    override suspend fun getChannelPins(channelId: ULong): List<ApiMessage> {
        return withContext(Dispatchers.IO) {
            if (cachedChannelPins[channelId].isEmpty()) {
                val url = getChannelPinsUrl(channelId)
                val response: List<ApiMessage> = client.get(url).body()
                cachedChannelPins[channelId].addAll(response)
            }
            cachedChannelPins[channelId]
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

    init {
        gateway.onEvent<MessageCreateEvent> {
            val data = it.data
            val channelId = data.channelId.value
            cachedChannelMessages[channelId].add(data)
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
    }

}