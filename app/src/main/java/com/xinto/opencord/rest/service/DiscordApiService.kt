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

    suspend fun getGuild(guildId: Long): ApiGuild

    suspend fun getGuildChannels(guildId: Long): List<ApiChannel>

    suspend fun getChannelMessages(channelId: Long): List<ApiMessage>

    suspend fun postChannelMessage(channelId: Long, body: MessageBody)

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

    override suspend fun getGuild(guildId: Long): ApiGuild {
        return withContext(Dispatchers.IO) {
            val url = getGuildUrl(guildId)
            client.get(url).body()
        }
    }

    override suspend fun getGuildChannels(guildId: Long): List<ApiChannel> {
        return withContext(Dispatchers.IO) {
            val url = getGuildChannelsUrl(guildId)
            client.get(url).body()
        }
    }

    override suspend fun getChannelMessages(channelId: Long): List<ApiMessage> {
        return withContext(Dispatchers.IO) {
            val url = getChannelMessagesUrl(channelId)
            client.get(url).body()
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

    private companion object {
        const val BASE = BuildConfig.URL_API

        fun getMeGuildsUrl(): String = "$BASE/users/@me/guilds"

        fun getGuildUrl(guildId: Long): String {
            return "$BASE/guilds/$guildId"
        }

        fun getGuildChannelsUrl(guildId: Long): String {
            val guildUrl = getGuildUrl(guildId)
            return "$guildUrl/channels"
        }

        fun getChannelMessagesUrl(channelId: Long): String {
            return "$BASE/channels/$channelId/messages"
        }
    }

}