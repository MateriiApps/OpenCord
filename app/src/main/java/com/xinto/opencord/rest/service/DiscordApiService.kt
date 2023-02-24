package com.xinto.opencord.rest.service

import com.xinto.opencord.BuildConfig
import com.xinto.opencord.domain.emoji.DomainEmoji
import com.xinto.opencord.domain.emoji.DomainGuildEmoji
import com.xinto.opencord.domain.emoji.DomainUnicodeEmoji
import com.xinto.opencord.domain.emoji.DomainUnknownEmoji
import com.xinto.opencord.rest.body.MessageBody
import com.xinto.opencord.rest.models.message.ApiMessage
import com.xinto.opencord.rest.models.user.settings.ApiUserSettings
import com.xinto.opencord.rest.models.user.settings.ApiUserSettingsPartial
import com.xinto.opencord.util.queryParameters
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
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
    suspend fun updateUserSettings(settings: ApiUserSettingsPartial): ApiUserSettings
    suspend fun startTyping(channelId: Long)

    suspend fun addMeReaction(channelId: Long, messageId: Long, emoji: DomainEmoji)
    suspend fun removeMeReaction(channelId: Long, messageId: Long, emoji: DomainEmoji)
}

class DiscordApiServiceImpl(
    private val client: HttpClient,
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

            client.get(url).body()
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

    override suspend fun addMeReaction(channelId: Long, messageId: Long, emoji: DomainEmoji) {
        withContext(Dispatchers.IO) {
            val url = getModifyReactionUrl(channelId, messageId, emoji, "@me")
            client.put(url)
        }
    }

    override suspend fun removeMeReaction(channelId: Long, messageId: Long, emoji: DomainEmoji) {
        withContext(Dispatchers.IO) {
            val url = getModifyReactionUrl(channelId, messageId, emoji, "@me")
            client.delete(url)
        }

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

        fun getModifyReactionUrl(
            channelId: Long,
            messageId: Long,
            emoji: DomainEmoji,
            user: String,
        ): String {
            val emojiId = when (emoji) {
                is DomainUnicodeEmoji -> emoji.emoji
                is DomainGuildEmoji -> "${emoji.name}:${emoji.id}"
                is DomainUnknownEmoji -> error("cannot remove an unknown emoji")
            }
            return "${getChannelUrl(channelId)}/messages/$messageId/reactions/$emojiId/$user"
        }
    }
}
