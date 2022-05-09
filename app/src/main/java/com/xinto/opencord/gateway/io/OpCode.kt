package com.xinto.opencord.gateway.io

import com.xinto.enumgetter.GetterGen
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(OpCode.Serializer::class)
@GetterGen
enum class OpCode(val code: Int) {
    Dispatch(0),
    Heartbeat(1),
    Identify(2),
    PresenceUpdate(3),
    VoiceStateUpdate(4),
    Resume(6),
    Reconnect(7),
    RequestGuildMembers(8),
    InvalidSession(9),
    Hello(10),
    HeartbeatAck(11);

    companion object Serializer : KSerializer<OpCode> {
        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("OpCode", PrimitiveKind.INT)

        override fun deserialize(decoder: Decoder): OpCode {
            val opCode = decoder.decodeInt()
            return fromValue(opCode) ?: throw IllegalArgumentException("Unknown OpCode $opCode")
        }

        override fun serialize(encoder: Encoder, value: OpCode) {
            encoder.encodeInt(value.code)
        }
    }
}