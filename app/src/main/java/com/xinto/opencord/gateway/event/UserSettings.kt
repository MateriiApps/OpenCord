package com.xinto.opencord.gateway.event

import com.xinto.opencord.rest.models.user.settings.ApiUserSettingsPartial

data class UserSettingsUpdateEvent(
    val data: ApiUserSettingsPartial
) : Event
