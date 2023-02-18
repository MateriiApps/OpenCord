package com.xinto.opencord.store

import com.xinto.opencord.db.database.AccountDatabase
import com.xinto.opencord.db.entity.EntityAccount
import com.xinto.opencord.domain.user.DomainUser
import com.xinto.opencord.domain.user.toDomain
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.manager.AccountManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

interface CurrentUserStore {
    fun observeCurrentUser(): Flow<DomainUser>

    suspend fun getCurrentUser(): DomainUser?
}

class CurrentUserStoreImpl(
    gateway: DiscordGateway,
    accountManager: AccountManager,
    accountDatabase: AccountDatabase,
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

                accountDatabase.accounts().insertAccount(
                    EntityAccount(
                        token = accountManager.currentAccountToken!!,
                        userId = it.id,
                        username = it.username,
                        discriminator = it.discriminator,
                        avatarUrl = it.avatarUrl,
                    ),
                )
            }
        }
    }
}
