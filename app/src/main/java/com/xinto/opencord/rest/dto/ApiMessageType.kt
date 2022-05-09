package com.xinto.opencord.rest.dto

import com.xinto.enumgetter.GetterGen
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(ApiMessageType.Serializer::class)
@GetterGen
enum class ApiMessageType(val value: Int) {
    Default(0),
    GuildMemberJoin(7);

    companion object Serializer : KSerializer<ApiMessageType> {
        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("ApiMessageType", PrimitiveKind.INT)

        override fun deserialize(decoder: Decoder): ApiMessageType {
            return fromValue(decoder.decodeInt()) ?: Default
        }

        override fun serialize(encoder: Encoder, value: ApiMessageType) {
            encoder.encodeInt(value.value)
        }
    }
}