package com.xinto.opencord.gateway.io

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(OpCode.Serializer::class)
enum class OpCode(val code: Int) {
    UNKNOWN(-1),

    DISPATCH(0),
    HEARTBEAT(1),
    IDENTIFY(2),
    PRESENCE_UPDATE(3),
    VOICE_STATE_UPDATE(4),
    RESUME(6),
    RECONNECT(7),
    REQUEST_GUILD_MEMBERS(8),
    INVALID_SESSION(9),
    HELLO(10),
    HEARTBEAT_ACK(11);

    companion object Serializer : KSerializer<OpCode> {
        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("OpCode", PrimitiveKind.INT)

        override fun deserialize(decoder: Decoder): OpCode {
            return fromCode(decoder.decodeInt())
        }

        override fun serialize(encoder: Encoder, value: OpCode) {
            encoder.encodeInt(value.code)
        }

        private fun fromCode(code: Int): OpCode {
            return when (code) {
                DISPATCH.code -> DISPATCH
                HEARTBEAT.code -> HEARTBEAT
                IDENTIFY.code -> IDENTIFY
                PRESENCE_UPDATE.code -> PRESENCE_UPDATE
                VOICE_STATE_UPDATE.code -> VOICE_STATE_UPDATE
                RESUME.code -> RESUME
                RECONNECT.code -> RECONNECT
                REQUEST_GUILD_MEMBERS.code -> REQUEST_GUILD_MEMBERS
                INVALID_SESSION.code -> INVALID_SESSION
                HELLO.code -> HELLO
                HEARTBEAT_ACK.code -> HEARTBEAT_ACK
                else -> UNKNOWN
            }
        }
    }
}