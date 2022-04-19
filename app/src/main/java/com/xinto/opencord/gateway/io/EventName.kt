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
    UNKNOWN(""),
    READY("READY"),
    GUILD_CREATE("GUILD_CREATE"),
    GUILD_UPDATE("GUILD_UPDATE"),
    GUILD_DELETE("GUILD_DELETE"),
    CHANNEL_CREATE("CHANNEL_CREATE"),
    CHANNEL_UPDATE("CHANNEL_UPDATE"),
    CHANNEL_DELETE("CHANNEL_DELETE"),
    MESSAGE_CREATE("MESSAGE_CREATE"),
    MESSAGE_UPDATE("MESSAGE_UPDATE"),
    MESSAGE_DELETE("MESSAGE_DELETE"),
    GUILD_MEMBER_CHUNK("GUILD_MEMBER_CHUNK");

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
                MESSAGE_UPDATE.eventName -> MESSAGE_UPDATE
                MESSAGE_DELETE.eventName -> MESSAGE_DELETE
                GUILD_CREATE.eventName -> GUILD_CREATE
                GUILD_UPDATE.eventName -> GUILD_UPDATE
                GUILD_DELETE.eventName -> GUILD_DELETE
                CHANNEL_CREATE.eventName -> CHANNEL_CREATE
                CHANNEL_UPDATE.eventName -> CHANNEL_UPDATE
                CHANNEL_DELETE.eventName -> CHANNEL_DELETE
                GUILD_MEMBER_CHUNK.eventName -> GUILD_MEMBER_CHUNK
                else -> UNKNOWN
            }
        }
    }
}