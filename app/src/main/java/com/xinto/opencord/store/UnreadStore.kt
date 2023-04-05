package com.xinto.opencord.store

import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.db.entity.channel.EntityUnreadState
import com.xinto.opencord.domain.channel.DomainUnreadState
import com.xinto.opencord.domain.channel.toDomain
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.ChannelDeleteEvent
import com.xinto.opencord.gateway.event.MessageAckEvent
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

typealias UnreadEvent = Event<DomainUnreadState, Nothing, Long>
typealias MentionCountEvent = Event<Int, Int, Nothing>

interface UnreadStore {
    /**
     * Observes new unread states for a channel.
     * Flow event details:
     * - Add -> A full replacement of an unread state
     * - Update -> Does not exist
     * - Delete -> Deletion of the observed channel (channelId)
     */
    fun observeUnreadState(channelId: Long): Flow<UnreadEvent>

    /**
     * Observes a change in the mention count for a channel.
     * Flow event details:
     * - Add -> The new replacement mention count
     * - Update -> The difference (+/-) in mention count
     * - Delete -> Does not exist
     */
    fun observeMentionCount(channelId: Long): Flow<MentionCountEvent>

    suspend fun getChannel(channelId: Long): DomainUnreadState?
}

class UnreadStoreImpl(
    gateway: DiscordGateway,
    private val cache: CacheDatabase,
) : UnreadStore {
    private val _unreadEvents = MutableSharedFlow<UnreadEvent>()
    private val _mentionCountEvents = MutableSharedFlow<Pair<Long, MentionCountEvent>>()

    private var currentUserId: Long? = null

    override fun observeUnreadState(channelId: Long): Flow<UnreadEvent> {
        return _unreadEvents.filter { event ->
            event.fold(
                onAdd = { it.channelId == channelId },
                onUpdate = { false },
                onDelete = { it == channelId },
            )
        }
    }

    override fun observeMentionCount(channelId: Long): Flow<MentionCountEvent> {
        return _mentionCountEvents
            .filter { (eventGuildId) -> channelId == eventGuildId }
            .map { (_, event) -> event }
    }

    override suspend fun getChannel(channelId: Long): DomainUnreadState? {
        return withContext(Dispatchers.IO) {
            cache.unreadStates().getUnreadState(channelId)?.toDomain()
        }
    }

    init {
        gateway.onEvent<ReadyEvent> { event ->
            val states = event.data.readState.entries.map {
                EntityUnreadState(
                    channelId = it.channelId,
                    mentionCount = it.mentionCount,
                    lastMessageId = it.lastMessageId,
                )
            }

            currentUserId = event.data.user.id.value
            states.forEach { _unreadEvents.emit(UnreadEvent.Add(it.toDomain())) }
            cache.unreadStates().replaceAllStates(states)
        }

        gateway.onEvent<MessageCreateEvent> { event ->
            val meMentioned = when {
                event.data.mentionEveryone -> true
                event.data.mentions.any { it.id.value == currentUserId } -> true
                else -> false
            }

            if (meMentioned) {
                val channelId = event.data.channelId.value
                _mentionCountEvents.emit(channelId to MentionCountEvent.Update(1))
                cache.unreadStates().incrementMentionCount(channelId)
            }
        }

        gateway.onEvent<MessageAckEvent> { event ->
            val state = EntityUnreadState(
                channelId = event.data.channelId,
                mentionCount = event.data.mentionCount ?: 0,
                lastMessageId = event.data.messageId,
            )

            _unreadEvents.emit(UnreadEvent.Add(state.toDomain()))
            cache.unreadStates().insertState(state)
        }

        gateway.onEvent<ChannelDeleteEvent> { event ->
            val channelId = event.data.id.value

            _unreadEvents.emit(UnreadEvent.Delete(channelId))
            cache.unreadStates().deleteUnreadState(channelId)
        }
    }
}
