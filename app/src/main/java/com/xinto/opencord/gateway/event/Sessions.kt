package com.xinto.opencord.gateway.event

import com.xinto.opencord.gateway.dto.ReplaceSessionsData

data class SessionsReplaceEvent(
    val data: ReplaceSessionsData,
) : Event
