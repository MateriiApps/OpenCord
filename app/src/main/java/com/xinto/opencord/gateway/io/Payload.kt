package com.xinto.opencord.gateway.io

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class IncomingPayload(
    @SerialName("op")
    val opCode: OpCode,

    @SerialName("d")
    val data: JsonElement?,

    @SerialName("s")
    val sequenceNumber: Int?,

    @SerialName("t")
    val eventName: EventName?,
)

@Serializable
data class OutgoingPayload<T>(
    @SerialName("op")
    val opCode: OpCode,

    @SerialName("d")
    val data: T?,
)