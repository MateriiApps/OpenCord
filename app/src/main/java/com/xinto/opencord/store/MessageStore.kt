package com.xinto.opencord.store

import com.github.materiiapps.partial.getOrNull
import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.db.entity.message.EntityMessage
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.mapper.toEntity
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.domain.model.DomainUser
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.event.MessageDeleteEvent
import com.xinto.opencord.gateway.event.MessageUpdateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.service.DiscordApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.withContext

interface MessageStore {
    fun observeChannel(channelId: Long): Flow<Event<DomainMessage>>

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
    private val events = MutableSharedFlow<Event<DomainMessage>>()

    override fun observeChannel(channelId: Long): Flow<Event<DomainMessage>> {
        return events.filter {
            it.data?.channelId == channelId
        }
    }

    private fun constructDomainMessage(
        message: EntityMessage,
        cachedUsers: MutableMap<Long, DomainUser?> = mutableMapOf()
    ): DomainMessage? {
        val attachments = if (!message.hasAttachments) null else {
            cache.attachments().getAttachments(message.id)
        }

        val referencedMessage = message.referencedMessageId?.let {
            cache.messages().getMessage(it)
        }

        val embeds = if (!message.hasEmbeds) null else {
            cache.embeds().getEmbeds(message.id)
        }

        val author = cachedUsers.computeIfAbsent(message.authorId) {
            cache.users().getUser(message.authorId)?.toDomain()
        } ?: return null

        return message.toDomain(
            author = author,
            referencedMessage = referencedMessage?.let { constructDomainMessage(it, cachedUsers) },
            embeds = embeds?.map { it.toDomain() },
            attachments = attachments?.map { it.toDomain() },
        )
    }

    override suspend fun fetchMessages(
        channelId: Long,
        after: Long?,
        around: Long?,
        before: Long?,
    ): List<DomainMessage> {
        return withContext(Dispatchers.IO) {
            val cachedMessages = when {
                after != null -> cache.messages().getMessagesAfter(channelId, 50, after)
                around != null -> cache.messages().getMessagesAround(channelId, 50, around)
                before != null -> cache.messages().getMessagesBefore(channelId, 50, before)
                else -> cache.messages().getMessagesLast(channelId, 50)
            }

            if (cachedMessages.size >= 50) {
                cachedMessages.mapNotNull(::constructDomainMessage)
            } else {
                val messages = api.getChannelMessages(
                    channelId = channelId,
                    limit = 50,
                    before = before,
                    around = around,
                    after = after,
                )

                cache.runInTransaction {
                    cache.users().apply {
                        val users = messages
                            .distinctBy { it.author.id }
                            .map { it.author.toEntity() }

                        insertUsers(users)
                    }

                    cache.messages().insertMessages(messages.map { it.toEntity() })

                    cache.attachments().insertAttachments(messages.flatMap { msg ->
                        msg.attachments.map {
                            it.toEntity(messageId = msg.id.value)
                        }
                    })

                    cache.embeds().insertEmbeds(messages.flatMap { msg ->
                        msg.embeds.mapIndexed { i, embed ->
                            embed.toEntity(
                                messageId = msg.id.value,
                                embedIndex = i,
                            )
                        }
                    })
                }

                messages.map { it.toDomain() }
            }
        }
    }

    init {
        gateway.onEvent<MessageCreateEvent> { event ->
            val message = event.data

            events.emit(Event.Add(message.toDomain()))

            cache.runInTransaction {
                cache.users().insertUsers(listOf(message.author.toEntity()))
                cache.messages().insertMessages(listOf(message.toEntity()))
                cache.attachments().insertAttachments(
                    message.attachments.map {
                        it.toEntity(message.id.value)
                    }
                )
                cache.embeds().insertEmbeds(message.embeds.mapIndexed { i, embed ->
                    embed.toEntity(
                        messageId = message.id.value,
                        embedIndex = i,
                    )
                })
            }
        }

        gateway.onEvent<MessageUpdateEvent> { event ->
            val message = event.data.id.getOrNull()?.value
                ?.let { cache.messages().getMessage(it) }
                ?.let { constructDomainMessage(it) }
                ?: return@onEvent

            // TODO: message update logic
        }

        gateway.onEvent<MessageDeleteEvent> {
            val messageId = it.data.messageId.value

            events.emit(Event.Remove(messageId))

            cache.runInTransaction {
                cache.messages().deleteMessage(messageId)
                cache.attachments().deleteAttachments(messageId)
                cache.embeds().deleteEmbeds(messageId)
            }
        }
    }
}