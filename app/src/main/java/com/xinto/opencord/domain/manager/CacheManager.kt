package com.xinto.opencord.domain.manager

import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.dto.SessionData
import com.xinto.opencord.gateway.event.SessionsReplaceEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.dto.ApiActivity

interface CacheManager {
    fun getSessions(): List<SessionData>
    fun getCurrentSession(): SessionData
    fun getActivities(): List<ApiActivity>
}

class CacheManagerImpl(
    val gateway: DiscordGateway,
) : CacheManager {
    private var _sessions: List<SessionData>? = null
    private var _activities: List<ApiActivity>? = null

    override fun getSessions(): List<SessionData> {
        return _sessions
            ?: throw IllegalStateException("No session data from gateway!")
    }

    override fun getActivities(): List<ApiActivity> {
        return _activities
            ?: throw IllegalStateException("No session data from gateway!")
    }

    override fun getCurrentSession(): SessionData {
        val gwSessionId = gateway.getSessionId()
        return getSessions().find { it.sessionId == gwSessionId }
            ?: throw IllegalStateException("Current session is not cached!")
    }

    init {
        gateway.onEvent<SessionsReplaceEvent> { e ->
            _sessions = e.data.filter { it.sessionId != "all" }
            _activities = _sessions!!.flatMap { it.activities }
        }
    }
}
