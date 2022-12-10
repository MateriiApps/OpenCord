package com.xinto.opencord.gateway.event

import com.xinto.opencord.rest.models.user.ApiUser

data class UserUpdateEvent(
    val data: ApiUser
) : Event
