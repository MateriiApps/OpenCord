package com.xinto.opencord.gateway.io

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(EventName.Serializer::class)
enum class EventName(val eventName: String) {
    READY("READY"),
    MESSAGE_CREATE("MESSAGE_CREATE"),
    GUILD_MEMBER_CHUNK("GUILD_MEMBER_CHUNK"),
    UNKNOWN("");

    companion object Serializer : KSerializer<EventName> {

        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("Event", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): EventName {
            return fromEventName(decoder.decodeString())
        }

        override fun serialize(encoder: Encoder, value: EventName) {
            encoder.encodeString(value.eventName)
        }

        private fun fromEventName(eventName: String): EventName {
            return when (eventName) {
                READY.eventName -> READY
                MESSAGE_CREATE.eventName -> MESSAGE_CREATE
                GUILD_MEMBER_CHUNK.eventName -> GUILD_MEMBER_CHUNK
                else -> UNKNOWN
            }
        }
    }
}