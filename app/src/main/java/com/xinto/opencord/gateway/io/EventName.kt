package com.xinto.opencord.gateway.io

import com.github.materiiapps.enumutil.FromValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@FromValue
@Serializable(EventName.Serializer::class)
enum class EventName(val eventName: String) {
    Ready("READY"),
    UserUpdate("USER_UPDATE"),
    GuildCreate("GUILD_CREATE"),
    GuildUpdate("GUILD_UPDATE"),
    GuildDelete("GUILD_DELETE"),
    ChannelCreate("CHANNEL_CREATE"),
    ChannelUpdate("CHANNEL_UPDATE"),
    ChannelDelete("CHANNEL_DELETE"),
    MessageCreate("MESSAGE_CREATE"),
    MessageUpdate("MESSAGE_UPDATE"),
    MessageDelete("MESSAGE_DELETE"),
    SessionsReplace("SESSIONS_REPLACE"),
    GuildMemberChunk("GUILD_MEMBER_CHUNK"),
    UserSettingsUpdate("USER_SETTINGS_UPDATE"),
    UserSettingsProtoUpdate("USER_SETTINGS_PROTO_UPDATE"),
    PresenceUpdate("PRESENCE_UPDATE");

    companion object Serializer : KSerializer<EventName> {

        override val descriptor: SerialDescriptor
            get() = PrimitiveSerialDescriptor("Event", PrimitiveKind.STRING)

        override fun deserialize(decoder: Decoder): EventName {
            val event = decoder.decodeString()
            return fromValue(event) ?: throw IllegalArgumentException("Unknown event $event")
        }

        override fun serialize(encoder: Encoder, value: EventName) {
            encoder.encodeString(value.eventName)
        }
    }
}
