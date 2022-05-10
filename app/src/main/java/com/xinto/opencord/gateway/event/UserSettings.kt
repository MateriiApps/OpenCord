package com.xinto.opencord.gateway.event

import com.xinto.opencord.rest.dto.ApiUserSettingsPartial

data class UserSettingsUpdateEvent(
    val data: ApiUserSettingsPartial
) : Event