package com.xinto.opencord.rest.dto

import com.xinto.enumgetter.GetterGen
import com.xinto.partialgen.Partial
import kotlinx.datetime.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
@Partial
data class ApiMessage(
    @SerialName("id")
    val id: ApiSnowflake,

    @SerialName("channel_id")
    val channelId: ApiSnowflake,

    @SerialName("timestamp")
    val timestamp: Instant,

    @SerialName("edited_timestamp")
    val editedTimestamp: Instant?,

    @SerialName("content")
    val content: String,

    @SerialName("author")
    val author: ApiUser,

    @SerialName("attachments")
    val attachments: List<ApiAttachment>,

    @SerialName("embeds")
    val embeds: List<ApiEmbed>,

    @SerialName("reactions")
    val reactions: List<ApiReaction> = emptyList(),

    @SerialName("type")
    val type: ApiMessageType
)


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