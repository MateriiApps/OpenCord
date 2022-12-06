package com.xinto.opencord.store

import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainUser
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

// TODO: figure out how to get generic domain user with private data (private/ready event user different)
interface CurrentUserStore {
    fun observeCurrentUser(): Flow<DomainUser>

    suspend fun getCurrentUser(): DomainUser?
}

class CurrentUserStoreImpl(
    gateway: DiscordGateway,
) : CurrentUserStore {
    private val events = MutableSharedFlow<DomainUser>(replay = 1)

    // TODO: implement db caching in refactor
    private var currentUser: DomainUser? = null

    override fun observeCurrentUser() = events
    override suspend fun getCurrentUser() = currentUser

    init {
        gateway.onEvent<ReadyEvent> { event ->
            event.data.user.toDomain().also {
                currentUser = it
                events.emit(it)
            }
        }
    }
}
