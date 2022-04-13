package com.xinto.opencord.rest.service

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.dto.ApiChannel
import com.xinto.opencord.rest.dto.ApiGuild
import com.xinto.opencord.rest.dto.ApiMeGuild
import com.xinto.opencord.rest.dto.ApiMessage
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

    suspend fun postChannelMessage(channelId: ULong, body: MessageBody)

}

class DiscordApiServiceImpl(
    private val client: HttpClient
) : DiscordApiService {

    override suspend fun getMeGuilds(): List<ApiMeGuild> {
        return withContext(Dispatchers.IO) {
            val url = getMeGuildsUrl()
            client.get(url).body()
        }
    }

    override suspend fun getGuild(guildId: ULong): ApiGuild {
        return withContext(Dispatchers.IO) {
            val url = getGuildUrl(guildId)
            client.get(url).body()
        }
    }

    override suspend fun getChannel(channelId: ULong): ApiChannel {
        return withContext(Dispatchers.IO) {
            val url = getChannelUrl(channelId)
            client.get(url).body()
        }
    }

    override suspend fun getGuildChannels(guildId: ULong): List<ApiChannel> {
        return withContext(Dispatchers.IO) {
            val url = getGuildChannelsUrl(guildId)
            client.get(url).body()
        }
    }

    override suspend fun getChannelMessages(channelId: ULong): List<ApiMessage> {
        return withContext(Dispatchers.IO) {
            val url = getChannelMessagesUrl(channelId)
            client.get(url).body()
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

    private companion object {
        const val BASE = BuildConfig.URL_API

        fun getMeGuildsUrl(): String = "$BASE/users/@me/guilds"

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
    }

}