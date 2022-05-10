package com.xinto.opencord.gateway.event

import com.xinto.opencord.gateway.dto.Ready

data class ReadyEvent(
    val data: Ready
) : Event