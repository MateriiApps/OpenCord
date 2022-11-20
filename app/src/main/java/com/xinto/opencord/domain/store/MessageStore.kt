package com.xinto.opencord.domain.store

import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.db.entity.message.EntityMessage
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.mapper.toEntity
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.event.MessageDeleteEvent
import com.xinto.opencord.gateway.event.MessageUpdateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.service.DiscordApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter

interface MessageStore {
    fun observeChannel(channelId: Long): Flow<Event<DomainMessage>>

    suspend fun fetchMessages(
        channelId: Long,
        after: Long? = null,
        before: Long? = null,
        around: Long? = null,
    ): List<DomainMessage>
}

class MessageStoreImpl(
    gateway: DiscordGateway,
    private val api: DiscordApiService,
    private val cache: CacheDatabase,
) : MessageStore {
    private val events = MutableSharedFlow<Event<DomainMessage>>()

    override fun observeChannel(channelId: Long): Flow<Event<DomainMessage>> {
        return events.filter {
            // Deletes should be passed through for future features like message link embeds
            it is Event.Remove || it.data?.channelId == channelId
        }
    }

    private fun constructDomainMessage(message: EntityMessage): DomainMessage {
        val attachments = if (!message.hasAttachments) null else {
            cache.messages().getAttachments(message.id)
        }

        val referencedMessage = message.referencedMessageId?.let {
            cache.messages().getMessage(it)
        }

        val embeds = if (!message.hasEmbeds) null else {
            cache.messages().getEmbeds(message.id)
        }

        return message.toDomain(
            author = null, // TODO
            referencedMessage = referencedMessage?.let { constructDomainMessage(it) },
            embeds = embeds?.map { it.toDomain() },
            attachments = attachments?.map { it.toDomain() },
        )
    }

    override suspend fun fetchMessages(
        channelId: Long,
        after: Long?,
        before: Long?,
        around: Long?,
    ): List<DomainMessage> {
        val cachedMessages = when {
            after != null -> cache.messages().getMessagesAfter(channelId, 50, after)
            before != null -> cache.messages().getMessagesBefore(channelId, 50, before)
            around != null -> cache.messages().getMessagesAround(channelId, 50, around)
            else -> throw IllegalArgumentException("after, before, around cannot all be null")
        }

        return if (cachedMessages.size >= 50) {
            cachedMessages.map(::constructDomainMessage)
        } else {
            val messages = api.getChannelMessages(channelId, before, after, around).values
            val entityMessages = messages.map { it.toEntity() }

            cache.messages().insertMessages(*entityMessages.toTypedArray())
            messages.map { it.toDomain() }
        }
    }

    init {
        gateway.onEvent<MessageCreateEvent> {
            events.emit(Event.Add(it.data.toDomain()))
            cache.messages().insertMessages(it.data.toEntity())
        }

        gateway.onEvent<MessageUpdateEvent> {
            // TODO: logic for update
        }

        gateway.onEvent<MessageDeleteEvent> {
            events.emit(Event.Remove(it.data.messageId.value))
            cache.messages().deleteMessages(it.data.messageId.value)
        }
    }
}
