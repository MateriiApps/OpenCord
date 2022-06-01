package com.xinto.opencord.gateway.event

import com.xinto.opencord.rest.dto.ApiUser

data class UserUpdateEvent(
    val data: ApiUser
) : Event
