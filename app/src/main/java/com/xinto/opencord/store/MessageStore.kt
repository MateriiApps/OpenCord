package com.xinto.opencord.store

import androidx.room.withTransaction
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.db.entity.message.EntityMessage
import com.xinto.opencord.db.entity.message.toEntity
import com.xinto.opencord.db.entity.user.toEntity
import com.xinto.opencord.domain.attachment.toDomain
import com.xinto.opencord.domain.embed.toDomain
import com.xinto.opencord.domain.message.DomainMessage
import com.xinto.opencord.domain.message.toDomain
import com.xinto.opencord.domain.user.DomainUser
import com.xinto.opencord.domain.user.toDomain
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.event.MessageDeleteEvent
import com.xinto.opencord.gateway.event.MessageUpdateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.models.message.ApiMessage
import com.xinto.opencord.rest.service.DiscordApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface MessageStore {
    fun observeMessage(id: Long): Flow<DomainMessage?>
    fun observeMessages(channelId: Long): Flow<List<DomainMessage>>

    suspend fun fetchPinnedMessages(channelId: Long): List<DomainMessage>
    suspend fun fetchMessages(
        channelId: Long,
        after: Long? = null,
        around: Long? = null,
        before: Long? = null,
    ): List<DomainMessage>
}

class MessageStoreImpl(
    gateway: DiscordGateway,
    private val api: DiscordApiService,
    private val cache: CacheDatabase,
) : MessageStore {
    private val messages = cache.messages()
    private val attachments = cache.attachments()
    private val embeds = cache.embeds()
    private val channels = cache.channels()
    private val users = cache.users()

    override fun observeMessage(id: Long): Flow<DomainMessage?> {
        return messages.observeMessage(id)
            .map {
                it?.let { message ->
                    constructDomainMessage(message)
                }
            }
    }

    override fun observeMessages(channelId: Long): Flow<List<DomainMessage>> {
        return messages.observeMessagesByChannelId(channelId).map { entityMessages ->
            entityMessages.mapNotNull {
                constructDomainMessage(it)
            }.sortedByDescending { it.id }
        }
    }

    private suspend fun constructDomainMessage(
        message: EntityMessage,
        cachedUsers: MutableMap<Long, DomainUser?> = mutableMapOf()
    ): DomainMessage? {
        return cache.withTransaction {
            val attachments = if (!message.hasAttachments) null else {
                attachments.getAttachmentsByMessageId(message.id)
            }

            val referencedMessage = message.referencedMessageId?.let {
                messages.getMessage(it)
            }

            val embeds = if (!message.hasEmbeds) null else {
                embeds.getEmbeds(message.id)
            }

            val author = if (cachedUsers[message.authorId] != null) {
                cachedUsers[message.authorId]!!
            } else {
                val user = users.getUser(message.authorId)?.toDomain()
                    ?: return@withTransaction null

                cachedUsers[message.authorId] = user

                user
            }
            message.toDomain(
                author = author,
                referencedMessage = referencedMessage?.let {
                    constructDomainMessage(it, cachedUsers)
                },
                embeds = embeds?.map { it.toDomain() },
                attachments = attachments?.map { it.toDomain() },
            )
        }
    }

    private suspend fun storeMessages(messages: List<ApiMessage>) {
        cache.withTransaction {
            users.apply {
                val users = messages
                    .distinctBy { it.author.id }
                    .map { it.author.toEntity() }

                insertUsers(users)
            }

            this.messages.insertMessages(messages.map { it.toEntity() })

            messages.forEachIndexed { index, message ->
                attachments.insertAttachments(
                    message.attachments.map { it.toEntity(message.id.value) }
                )
                embeds.insertEmbeds(
                    message.embeds.map { it.toEntity(message.id.value, index) }
                )
            }
        }
    }

    private suspend fun storeMessage(message: ApiMessage) {
        cache.withTransaction {
            users.insertUser(message.author.toEntity())

            this.messages.insertMessage(message.toEntity())

            attachments.insertAttachments(
                message.attachments.map {
                    it.toEntity(messageId = message.id.value)
                }
            )

            embeds.insertEmbeds(
                message.embeds.mapIndexed { i, embed ->
                    embed.toEntity(
                        messageId = message.id.value,
                        embedIndex = i,
                    )
                },
            )
        }
    }

    override suspend fun fetchPinnedMessages(channelId: Long): List<DomainMessage> {
        return withContext(Dispatchers.IO) {
            val pinsStored = cache.channels().isChannelPinsStored(channelId)
                ?: false

            if (pinsStored) {
                messages.getPinnedMessages(channelId)
                    .mapNotNull { constructDomainMessage(it) }
            } else {
                val messages = api.getChannelPins(channelId)

                storeMessages(messages)
                channels.setChannelPinsStored(channelId, true)
                messages.map { it.toDomain() }
            }
        }
    }

    override suspend fun fetchMessages(
        channelId: Long,
        after: Long?,
        around: Long?,
        before: Long?,
    ): List<DomainMessage> {
        return withContext(Dispatchers.IO) {
            val cachedMessages = when {
                after != null -> messages.getMessagesAfter(channelId, 50, after)
                around != null -> messages.getMessagesAround(channelId, 50, around)
                before != null -> messages.getMessagesBefore(channelId, 50, before)
                else -> messages.getMessagesLast(channelId, 50)
            }

            if (cachedMessages.size >= 50) {
                cachedMessages.mapNotNull { constructDomainMessage(it) }
            } else {
                val messages = api.getChannelMessages(
                    channelId = channelId,
                    limit = 50,
                    before = before,
                    around = around,
                    after = after,
                )

                if (messages.size > 1) {
                    storeMessages(messages)
                } else if (messages.size == 1) {
                    storeMessage(messages[0])
                }
                messages.map { it.toDomain() }
            }
        }
    }

    init {
        gateway.onEvent<MessageCreateEvent> { event ->
            val message = event.data

            storeMessage(message)
        }

        gateway.onEvent<MessageUpdateEvent> { event ->
            // TODO: figure out message updates + cache updating
        }

        gateway.onEvent<MessageDeleteEvent> {
            val messageId = it.data.messageId.value

            cache.withTransaction {
                messages.deleteMessage(messageId)
                attachments.deleteAttachmentsByMessageId(messageId)
                embeds.deleteEmbeds(messageId)
            }
        }
    }
}
