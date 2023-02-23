package com.xinto.opencord.store

import com.xinto.opencord.db.database.CacheDatabase
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.ChannelDeleteEvent
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.withContext

/**
 * A pair of channelId -> lastMessageId
 */
typealias LastMessageEventData = Pair<Long, Long>
typealias LastMessageEvent = Event<LastMessageEventData, Nothing, Long>

interface LastMessageStore {
    fun observeChannel(channelId: Long): Flow<LastMessageEvent>

    suspend fun getLastMessageId(channelId: Long): Long?
}

class LastMessageStoreImpl(
    gateway: DiscordGateway,
    private val cache: CacheDatabase,
) : LastMessageStore {
    private val _events = MutableSharedFlow<LastMessageEvent>()

    override fun observeChannel(channelId: Long): Flow<LastMessageEvent> {
        return _events.filter { event ->
            event.fold(
                onAdd = { (id) -> id == channelId },
                onUpdate = { false },
                onDelete = { id -> id == channelId },
            )
        }
    }

    override suspend fun getLastMessageId(channelId: Long): Long? {
        return withContext(Dispatchers.IO) {
            cache.channels().getLastMessageId(channelId)
        }
    }

    init {
        gateway.onEvent<ReadyEvent> { event ->
            for (guild in event.data.guilds) {
                for (channel in guild.channels) {
                    val lastMessageId = channel.lastMessageId ?: continue
                    _events.emit(LastMessageEvent.Add(channel.id.value to lastMessageId.value))
                }
            }

            // cache updated by inserting channels in ChannelStore (lastMessageId lives on EntityChannel)
        }

        gateway.onEvent<MessageCreateEvent> { event ->
            val messageId = event.data.id.value
            val channelId = event.data.channelId.value

            _events.emit(LastMessageEvent.Add(channelId to messageId))
            cache.channels().setLastMessageId(channelId, messageId)
        }

        gateway.onEvent<ChannelDeleteEvent> { event ->
            _events.emit(LastMessageEvent.Delete(event.data.id.value))
        }
    }
}
