package com.xinto.opencord.rest.dto

import com.xinto.enumgetter.GetterGen
import com.xinto.partialgen.Partialable
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
@Partialable
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

    @SerialName("type")
    val type: ApiMessageType,

    @SerialName("referenced_message")
    val referencedMessage: ApiMessage? = null,

    @SerialName("mention_everyone")
    val mentionEveryone: Boolean,

    // TODO: add mention role support
//    @SerialName("mention_roles")
//    val mentionRoles: List<ApiRole>,

    @SerialName("mentions")
    val mentions: List<ApiUser>,
)

@Serializable(ApiMessageType.Serializer::class)
@GetterGen
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