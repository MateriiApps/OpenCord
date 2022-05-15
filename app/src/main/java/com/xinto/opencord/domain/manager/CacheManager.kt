package com.xinto.opencord.domain.manager

import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainActivity
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.dto.SessionData
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.event.SessionsReplaceEvent
import com.xinto.opencord.gateway.onEvent

interface CacheManager {
    fun getSessions(): List<SessionData>
    fun getCurrentSession(): SessionData

    fun getActivities(): List<DomainActivity>
}

class CacheManagerImpl(
    val gateway: DiscordGateway,
) : CacheManager {
    private var _sessions: List<SessionData>? = null
    private var _activities: List<DomainActivity>? = null

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
    }
}
