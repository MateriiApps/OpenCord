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
    suspend fun getChannelPins(channelId: Long): List<ApiMessage>
    suspend fun getChannelMessages(
        channelId: Long,
        limit: Long,
        before: Long? = null,
        after: Long? = null,
        around: Long? = null,
    ): List<ApiMessage>

    suspend fun postChannelMessage(channelId: Long, body: MessageBody)

    suspend fun getUserSettings(): ApiUserSettings
    suspend fun updateUserSettings(settings: ApiUserSettingsPartial): ApiUserSettings

    suspend fun startTyping(channelId: Long)
}

class DiscordApiServiceImpl(
    private val client: HttpClient
) : DiscordApiService {
    override suspend fun getChannelMessages(
        channelId: Long,
        limit: Long,
        before: Long?,
        after: Long?,
        around: Long?,
    ): List<ApiMessage> {
        return withContext(Dispatchers.IO) {
            client
                .get(getChannelMessagesUrl(channelId, before, after, around))
                .body()
        }
    }

    override suspend fun getChannelPins(channelId: Long): List<ApiMessage> {
        return withContext(Dispatchers.IO) {
            val url = getChannelPinsUrl(channelId)
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

    override suspend fun getUserSettings(): ApiUserSettings {
        return withContext(Dispatchers.IO) {
            client.get(getUserSettingsUrl()).body()
        }
    }

    override suspend fun updateUserSettings(settings: ApiUserSettingsPartial): ApiUserSettings {
        return withContext(Dispatchers.IO) {
            client.patch(getUserSettingsUrl()) {
                setBody(settings)
            }.body()
        }
    }

    override suspend fun startTyping(channelId: Long) {
        withContext(Dispatchers.IO) {
            val url = getTypingUrl(channelId)
            client.post(url)
        }
    }

    init {
        // TODO: move this to user settings store
//        gateway.onEvent<UserSettingsUpdateEvent> {
//            cachedUserSettings = cachedUserSettings?.merge(it.data)
//        }
    }

    private companion object {
        const val BASE = BuildConfig.URL_API

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
