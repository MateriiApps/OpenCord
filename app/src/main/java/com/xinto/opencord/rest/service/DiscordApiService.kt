package com.xinto.opencord.rest.service

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.manager.AccountManager
import com.xinto.opencord.domain.provider.TelemetryProvider
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.UserSettingsUpdateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.dto.*
import com.xinto.opencord.util.queryParameters
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface DiscordApiService {
    suspend fun getChannelPins(channelId: Long): List<ApiMessage>
    suspend fun getChannelMessages(
        channelId: Long,
        limit: Long = 50,
        before: Long? = null,
        around: Long? = null,
        after: Long? = null,
    ): List<ApiMessage>

    suspend fun postChannelMessage(channelId: Long, body: MessageBody)

    suspend fun getUserSettings(): ApiUserSettings
    suspend fun updateUserSettings(settings: ApiUserSettingsPartial): ApiUserSettings

    suspend fun startTyping(channelId: Long)
}

class DiscordApiServiceImpl(
    private val client: HttpClient,
    private val accountManager: AccountManager,
    private val telemetryProvider: TelemetryProvider
) : DiscordApiService {
    override suspend fun getChannelMessages(
        channelId: Long,
        limit: Long,
        before: Long?,
        around: Long?,
        after: Long?,
    ): List<ApiMessage> {
        return withContext(Dispatchers.IO) {
            val url = getChannelMessagesUrl(
                channelId = channelId,
                limit = limit,
                before = before,
                around = around,
                after = after,
            )

            authedGet(url).body()
        }
    }

    override suspend fun getChannelPins(channelId: Long): List<ApiMessage> {
        return withContext(Dispatchers.IO) {
            val url = getChannelPinsUrl(channelId)
            authedGet(url).body()
        }
    }

    override suspend fun postChannelMessage(channelId: Long, body: MessageBody) {
        withContext(Dispatchers.IO) {
            val url = getChannelMessagesUrl(channelId)
            authedPost(url) {
                setBody(body)
            }
        }
    }

    override suspend fun getUserSettings(): ApiUserSettings {
        return withContext(Dispatchers.IO) {
            authedGet(getUserSettingsUrl()).body()
        }
    }

    override suspend fun updateUserSettings(settings: ApiUserSettingsPartial): ApiUserSettings {
        return withContext(Dispatchers.IO) {
            authedPatch(getUserSettingsUrl()) {
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

    private suspend inline fun authedGet(
        url: String,
        httpRequestBuilder: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        return client.get(url) {
            authedHttpRequest()
            httpRequestBuilder()
        }
    }
    
    private suspend inline fun authedPost(
        url: String,
        httpRequestBuilder: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        return client.post(url) {
            authedHttpRequest()
            httpRequestBuilder()
        }
    }
    
    private suspend inline fun authedPatch(
        url: String,
        httpRequestBuilder: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        return client.patch(url) {
            authedHttpRequest()
            httpRequestBuilder()
        }
    }
    
    private fun HttpRequestBuilder.authedHttpRequest() {
        header(HttpHeaders.Authorization, accountManager.currentAccountToken!!)
        header(HttpHeaders.UserAgent, telemetryProvider.userAgent)
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
            around: Long? = null,
            after: Long? = null,
        ): String {
            return getChannelUrl(channelId) + "/messages" + queryParameters {
                before?.let { append("before", it.toString()) }
                around?.let { append("around", it.toString()) }
                after?.let { append("after", it.toString()) }
                limit?.let { append("limit", limit.toString()) }
            }
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
