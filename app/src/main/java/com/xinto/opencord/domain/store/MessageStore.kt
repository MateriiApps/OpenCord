package com.xinto.opencord.domain.store

import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.MessageCreateEvent
import com.xinto.opencord.gateway.event.MessageDeleteEvent
import com.xinto.opencord.gateway.event.MessageUpdateEvent
import com.xinto.opencord.gateway.onEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onSubscription

interface MessageStore {

    fun observe(channelId: Long): Flow<ListEvent<DomainMessage>>

    suspend fun fetch(channelId: Long)

}

class MessageStoreImpl(
    gateway: DiscordGateway,
    private val discordApiRepository: DiscordApiRepository
): MessageStore {

    private val events = MutableSharedFlow<ListEvent<DomainMessage>>()

    override fun observe(channelId: Long): Flow<ListEvent<DomainMessage>> {
        return events.onSubscription {
            //This should only emit to the current subscription, not the whole events.
            //Otherwise, it'll cause unnecessary emits and then filtration
            discordApiRepository.getChannelMessages(channelId).values.forEach {
                emit(ListEvent.Add(it))
            }
        }.filter { it.data.channelId == channelId }
    }

    override suspend fun fetch(channelId: Long) {
        //TODO
    }

    init {
        gateway.onEvent<MessageCreateEvent> {
            events.emit(ListEvent.Add(it.data.toDomain()))
        }
        gateway.onEvent<MessageUpdateEvent> {
            //TODO logic for update
        }
        gateway.onEvent<MessageDeleteEvent> {
            //TODOD logic for delete
        }
    }

}