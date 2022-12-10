package com.xinto.opencord.store

import com.xinto.opencord.domain.activity.DomainActivity
import com.xinto.opencord.domain.activity.toDomain
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.dto.SessionData
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.event.SessionsReplaceEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.dto.ApiActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

// TODO: proper session domain models in refactor
interface SessionStore {
    fun observeSessions(): Flow<List<SessionData>>
    fun observeCurrentSession(): Flow<SessionData>
    fun observeActivities(): Flow<List<DomainActivity>>

    fun getActivities(): List<DomainActivity>?
    fun getCurrentSession(): SessionData?
}

class SessionStoreImpl(
    gateway: DiscordGateway,
) : SessionStore {
    private val events = MutableSharedFlow<List<SessionData>>(replay = 1)
    private val activityEvents = MutableSharedFlow<List<DomainActivity>>()

    private var _sessionId: String? = null
    private var _currentSession: SessionData? = null
    private var _activities: List<DomainActivity>? = null

    override fun observeSessions() = events

    override fun observeCurrentSession(): Flow<SessionData> {
        return events.map { event ->
            event.find { it.sessionId == _sessionId }
                ?: throw IllegalStateException("Active session not found in sessions list")
        }
    }

    override fun observeActivities() = activityEvents
    override fun getActivities() = _activities
    override fun getCurrentSession() = _currentSession

    private suspend fun handleSessionsUpdate(sessions: List<SessionData>) {
        val newSessions = sessions
            .filter { it.sessionId != "all" }
            .also { events.emit(it) }

        newSessions
            .flatMap { it.activities }
            .map(ApiActivity::toDomain)
            .also { _activities = it }
            .also { activityEvents.emit(it) }

        _currentSession = newSessions
            .find { it.sessionId == _sessionId }
    }

    init {
        gateway.onEvent<ReadyEvent> { event ->
            _sessionId = event.data.sessionId
            handleSessionsUpdate(event.data.sessions)
        }

        gateway.onEvent<SessionsReplaceEvent> {
            handleSessionsUpdate(it.data)
        }
    }
}
