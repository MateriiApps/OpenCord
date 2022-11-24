package com.xinto.opencord.store

import com.xinto.opencord.domain.mapper.toApi
import com.xinto.opencord.domain.mapper.toDomain
import com.xinto.opencord.domain.model.DomainUserSettings
import com.xinto.opencord.domain.model.DomainUserSettingsPartial
import com.xinto.opencord.domain.model.merge
import com.xinto.opencord.gateway.DiscordGateway
import com.xinto.opencord.gateway.event.ReadyEvent
import com.xinto.opencord.gateway.event.UserSettingsUpdateEvent
import com.xinto.opencord.gateway.onEvent
import com.xinto.opencord.rest.service.DiscordApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext

interface UserSettingsStore {
    fun observeUserSettings(): Flow<DomainUserSettings>

    suspend fun getUserSettings(): DomainUserSettings?
    suspend fun updateUserSettings(settings: DomainUserSettingsPartial): DomainUserSettings
}

class UserSettingsStoreImpl(
    gateway: DiscordGateway,
    private val api: DiscordApiService,
) : UserSettingsStore {
    private val events = MutableSharedFlow<DomainUserSettings>(replay = 1)

    // TODO: implement db caching in refactor
    private var userSettings: DomainUserSettings? = null

    override fun observeUserSettings() = events
    override suspend fun getUserSettings() = userSettings

    override suspend fun updateUserSettings(settings: DomainUserSettingsPartial): DomainUserSettings {
        return withContext(Dispatchers.IO) {
            val newSettings = api.updateUserSettings(settings.toApi())

            newSettings.toDomain().also {
                userSettings = it
                events.emit(it)
            }
        }
    }

    init {
        gateway.onEvent<ReadyEvent> { event ->
            event.data.userSettings.toDomain().also {
                userSettings = it
                events.emit(it)
            }
        }

        gateway.onEvent<UserSettingsUpdateEvent> { event ->
            userSettings?.merge(event.data.toDomain())?.also {
                userSettings = it
                events.emit(it)
            }
        }
    }
}
