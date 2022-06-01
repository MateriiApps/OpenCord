package com.xinto.opencord.domain.manager

import com.xinto.opencord.db.database.AppDatabase
import com.xinto.opencord.domain.mapper.toApi
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.mapper.toEntity
import com.xinto.opencord.domain.model.DomainActivity
import com.xinto.opencord.domain.model.DomainMessage
import com.xinto.opencord.domain.model.base.DomainModel
import com.xinto.opencord.domain.repository.DiscordApiRepository
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.dto.SessionData
import com.xinto.opencord.gateway.event.*
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.dto.merge
import com.xinto.partialgen.getOrNull
import kotlinx.coroutines.flow.*

sealed interface Event<out T> {
    val model: T

    class Create<out T>(override val model: T): Event<T>
    class Update<out T>(override val model: T): Event<T>
    class Delete<out T>(override val model: T): Event<T>
}

interface CacheManager {
    fun getSessions(): List<SessionData>
    fun getCurrentSession(): SessionData

    fun getActivities(): List<DomainActivity>

    fun retrieveMessages(channelId: ULong): Flow<Event<DomainMessage>>
}

class CacheManagerImpl(
    private val gateway: DiscordGateway,
    private val repository: DiscordApiRepository,
    private val database: AppDatabase
) : CacheManager {

    private var _sessions: List<SessionData>? = null
    private var _activities: List<DomainActivity>? = null

    private val messagesDao = database.messagesDao()

    private val events = MutableSharedFlow<Event<DomainModel>>()

    override fun getSessions(): List<SessionData> {
        return _sessions
            ?: throw IllegalStateException("No session data from gateway!")
    }

    override fun getActivities(): List<DomainActivity> {
        return _activities
            ?: throw IllegalStateException("No session data from gateway!")
    }

    override fun getCurrentSession(): SessionData {
        val sessionId = gateway.getSessionId()
        return getSessions().find { it.sessionId == sessionId }
            ?: throw IllegalStateException("Current session is not cached!")
    }

    override fun retrieveMessages(channelId: ULong): Flow<Event<DomainMessage>> {
        return events
            .onSubscription {
                repository.getChannelMessages(channelId).forEach {
                    emit(Event.Create(it))
                }
            }
            .filterIsInstance<Event<DomainMessage>>()
            .filter { it.model.channelId == channelId }
    }

    private fun handleSessions(sessions: List<SessionData>) {
        _sessions = sessions.filter { it.sessionId != "all" }
        _activities = _sessions!!
            .flatMap { it.activities }
            .map { it.toDomain() }
    }

    init {
        gateway.onEvent<ReadyEvent> {
            handleSessions(it.data.sessions)
        }
        gateway.onEvent<SessionsReplaceEvent> {
            handleSessions(it.data)
        }
        gateway.onEvent<MessageCreateEvent> {
            val entityMessage = it.data.toEntity()
            val domainMessage = it.data.toDomain()

            if (messagesDao.getAllMessages().isNotEmpty()) {
                messagesDao.insert(entityMessage)
                events.emit(Event.Create(domainMessage))
            }
        }
        gateway.onEvent<MessageUpdateEvent> {
            //horror
            val messageId = it.data.id.getOrNull()?.value!!
            val localEntityMessage = messagesDao.getMessageById(messageId.toLong())
            val localApiMessage = localEntityMessage?.toApi()
            val mergedApiMessage = localApiMessage?.merge(it.data)
            val mergedEntityMessage = mergedApiMessage?.toEntity()
            val mergedDomainMessage = localApiMessage?.toDomain()

            if (mergedEntityMessage != null && mergedDomainMessage != null) {
                messagesDao.update(mergedEntityMessage)
                events.emit(Event.Update(mergedDomainMessage))
            }
        }
        gateway.onEvent<MessageDeleteEvent> {
            val messageId = it.data.messageId.value
            val localEntityMessage = messagesDao.getMessageById(messageId.toLong())
            val localDomainMessage = localEntityMessage?.toDomain()
            if (localEntityMessage != null && localDomainMessage != null) {
                messagesDao.delete(localEntityMessage)
                events.emit(Event.Delete(localDomainMessage))
            }
        }
    }
}
