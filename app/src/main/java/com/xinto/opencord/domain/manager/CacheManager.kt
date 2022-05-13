package com.xinto.opencord.domain.manager

import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.onEvent

interface CacheManager {
    fun getSessions(): List<Any>
}

class CacheManagerImpl(
    val gateway: DiscordGateway,
) : CacheManager {
    private val sessions = emptyList<Any>()

    override fun getSessions(): List<Any> {
        TODO("Not yet implemented")
    }

    init {
        sessions = gateway.onEvent<>()
    }
}
