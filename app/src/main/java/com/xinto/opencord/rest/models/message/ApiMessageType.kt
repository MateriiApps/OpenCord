package com.xinto.opencord.rest.models.message

import com.github.materiiapps.enumutil.FromValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@FromValue
@Serializable(ApiMessageType.Serializer::class)
enum class ApiMessageType(val value: Int) {
    Default(0),
    GuildMemberJoin(7),
    Reply(19);

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
