package com.xinto.opencord.gateway.event

import com.xinto.opencord.gateway.dto.MessageReactionAddData
import com.xinto.opencord.gateway.dto.MessageReactionRemoveAllData
import com.xinto.opencord.gateway.dto.MessageReactionRemoveData

data class MessageReactionAddEvent(
    val data: MessageReactionAddData
) : Event

data class MessageReactionRemoveEvent(
    val data: MessageReactionRemoveData
) : Event

data class MessageReactionRemoveAllEvent(
    val data: MessageReactionRemoveAllData,
) : Event
